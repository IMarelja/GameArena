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
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberHighPrivilegeAddRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberView;
import hr.algebra.gamearena.api.exceptions.extenders.BadRequestedException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.service.tournament.ITournamentService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
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

    // Tournament

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<TournamentFullView>>> getTournaments() {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getAllTournaments()));
    }


    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<TournamentFullView>> getTournament(@PathVariable Long id) {
        var tournament = this.tournamentService.getTournament(id);

        return tournament.map( tournamentFullView -> ResponseEntity.ok(ApiResponse.success(tournamentFullView)))
                .orElseThrow(() -> new NotFoundException("Tournament with id: " + id + " not found"));
    }

    @PostMapping
    @PreAuthorize("permitAll()") // later ADMIN only
    public ResponseEntity<ApiResponse<TournamentFullView>> createTournament(
            @Valid @RequestBody TournamentCreateRequest request
    ) {
        if(!request.getStartsAt().getOffset().equals(ZoneOffset.UTC))
            throw new BadRequestedException("Starts at offset is not UTC or 00+00, it is: " + request.getStartsAt().getOffset());

        if(request.getEndsAt() != null && !request.getEndsAt().getOffset().equals(ZoneOffset.UTC))
            throw new BadRequestedException("Ends at offset is not UTC or 00+00, it is: " + request.getEndsAt().getOffset());

        return ResponseEntity.ok(ApiResponse.success(tournamentService.createTournament(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("permitAll()") // later ADMIN only
    public ResponseEntity<ApiResponse<TournamentFullView>> updateTournament(
            @PathVariable Long id,
            @Valid @RequestBody TournamentEditRequest request
    ) {
        if(!request.getStartsAt().getOffset().equals(ZoneOffset.UTC))
            throw new BadRequestedException("Starts at offset is not UTC or 00+00, it is: " + request.getStartsAt().getOffset());

        if(request.getEndsAt() != null && !request.getEndsAt().getOffset().equals(ZoneOffset.UTC))
            throw new BadRequestedException("Ends at offset is not UTC or 00+00, it is: " + request.getEndsAt().getOffset());

        return ResponseEntity.ok(ApiResponse.success(tournamentService.updateTournament(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTournament(
            @PathVariable Long id
    ) {
        this.tournamentService.deleteTournament(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Tournament Member's

    @GetMapping("/{tournamentId}/members")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<TournamentMemberView>>> getMembersOfTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.getTournamentsMembers(tournamentId)));
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

    @PostMapping("/{tournamentId}/member")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TournamentMemberView>> addTournamentMember(
            @PathVariable Long tournamentId,
            @Valid @RequestBody TournamentMemberHighPrivilegeAddRequest request
    ){
        request.setTournamentId(tournamentId);

        return ResponseEntity.ok(ApiResponse.success(tournamentService.addTournamentMemberAsAHighPrivilege(request)));
    }

    @PatchMapping("/member/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TournamentMemberView>> editTournamentMember(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtTokenClaim caller,
            @Valid @RequestBody TournamentMemberEditRequest request
    ){
        return ResponseEntity.ok(ApiResponse.success(tournamentService.editTournamentMember(caller.userId(), id, request)));
    }

    @DeleteMapping("/member/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeTournamentMember(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtTokenClaim caller
    ){
        tournamentService.removeTournamentMember(caller.userId(), id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
