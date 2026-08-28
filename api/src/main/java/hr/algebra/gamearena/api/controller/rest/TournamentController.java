package hr.algebra.gamearena.api.controller.rest;
import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentEditRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentFullView;
import hr.algebra.gamearena.api.dto.payment.responce.PaymentResponseView;
import hr.algebra.gamearena.api.dto.payment.responce.PaymentStagesView;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberEditRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberView;
import hr.algebra.gamearena.api.exceptions.extenders.BadRequestedException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.service.tournament.ITournamentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/tournament")
public class TournamentController {

    private final ITournamentService tournamentService;

    public TournamentController(ITournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    /** Tournament */
    // BEGIN

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<TournamentFullView>>> getTournaments() {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getAllTournaments()));
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TournamentFullView>>> getMyTournaments(@AuthenticationPrincipal JwtTokenClaim caller) {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getTournamentsFromUserId(caller.userId())));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<TournamentFullView>>> getTournamentsForUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getTournamentsFromUserId(id)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<TournamentFullView>> getTournament(@PathVariable Long id) {
        var tournament = this.tournamentService.getTournament(id);

        return tournament.map( tournamentFullView -> ResponseEntity.ok(ApiResponse.success(tournamentFullView)))
                .orElseThrow(() -> new NotFoundException("Tournament with id: " + id + " not found"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TournamentFullView>> createTournament(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @Valid @RequestBody TournamentCreateRequest request
    ) {
        if(!request.getStartsAt().getOffset().equals(ZoneOffset.UTC))
            throw new BadRequestedException("Starts at offset is not UTC or 00+00, it is: " + request.getStartsAt().getOffset());

        if(request.getEndsAt() != null && !request.getEndsAt().getOffset().equals(ZoneOffset.UTC))
            throw new BadRequestedException("Ends at offset is not UTC or 00+00, it is: " + request.getEndsAt().getOffset());

        return ResponseEntity.ok(ApiResponse.success(tournamentService.createTournament(caller.userId(), request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    // Admin or Tournament Organizer only
    public ResponseEntity<ApiResponse<TournamentFullView>> editTournament(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long id,
            @Valid @RequestBody TournamentEditRequest request
    ) {
        if(!request.getStartsAt().getOffset().equals(ZoneOffset.UTC))
            throw new BadRequestedException("Starts at offset is not UTC or 00+00, it is: " + request.getStartsAt().getOffset());

        if(request.getEndsAt() != null && !request.getEndsAt().getOffset().equals(ZoneOffset.UTC))
            throw new BadRequestedException("Ends at offset is not UTC or 00+00, it is: " + request.getEndsAt().getOffset());

        return ResponseEntity.ok(ApiResponse.success(tournamentService.editTournamentAsOrganizerOrAdmin(caller.userId(), id, request)));
    }

    // END

    /** Tournament Member's */
    // BEGIN
    @GetMapping("/{tournamentId}/members")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<TournamentMemberView>>> getMembersOfTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getTournamentsMembers(tournamentId)));
    }

    @GetMapping("/{tournamentId}/member/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TournamentMemberView>> getMeMemberOfTournament(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long tournamentId
    ){
        return tournamentService.getTournamentMemberByUserIdAndTournamentId(caller.userId(), tournamentId)
                .map(member -> ResponseEntity.ok(ApiResponse.success(member)))
                .orElseThrow(() -> new NotFoundException("You are not a member of tournament with id: " + tournamentId));
    }

    @GetMapping("/{tournamentId}/member/{memberId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<TournamentMemberView>> getMemberOfTournament(
            @PathVariable Long tournamentId,
            @PathVariable Long memberId
    ){
        return tournamentService.getTournamentMemberByIdAndTournamentId(memberId, tournamentId)
                .map(member -> ResponseEntity.ok(ApiResponse.success(member)))
                .orElseThrow(() -> new NotFoundException("Tournament member (" + memberId + ") not found in tournament with id: " + tournamentId));
    }

    @PostMapping("/{tournamentId}/join")
    @PreAuthorize("isAuthenticated()")
    public Flux<ServerSentEvent<ApiResponse<PaymentResponseView>>> joinTournament(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long tournamentId,
            @Valid @RequestBody PaymentRequest paymentRequest
    ) {
        return tournamentService.joinAsRegularTournamentMemberAndPay(caller.userId(), tournamentId, paymentRequest)
                .map(status -> {
                    boolean isTransactionError = status.stage() == PaymentStagesView.FAILED
                            || status.stage() == PaymentStagesView.TIME_OUT;

                    ApiResponse<PaymentResponseView> response;
                    if (isTransactionError) {
                        response = ApiResponse.errorDataOnly(status);
                    } else {
                        response = ApiResponse.success(status);
                    }

                    return ServerSentEvent.<ApiResponse<PaymentResponseView>>builder(response)
                            .event("tournament-join-status")
                            .build();
                });
    }

    @PostMapping("/{tournamentId}/member/organizer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TournamentMemberView>> addTournamentMember(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long tournamentId,
            @Valid @RequestBody TournamentMemberCreateRequest request
    ){
        return ResponseEntity.ok(ApiResponse.success(tournamentService.addTournamentMemberAsOrganizerOrAdmin(caller.userId(), tournamentId, request)));
    }

    @PatchMapping("/member/{id}")
    @PreAuthorize("isAuthenticated()")
    // Admin or Tournament Organizer only
    public ResponseEntity<ApiResponse<TournamentMemberView>> editTournamentMember(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long id,
            @Valid @RequestBody TournamentMemberEditRequest request
    ){
        return ResponseEntity.ok(ApiResponse.success(tournamentService.editTournamentMemberAsOrganizerOrAdmin(caller.userId(), id, request)));
    }

    //END
}
