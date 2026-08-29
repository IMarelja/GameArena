package hr.algebra.gamearena.webapp.service.tournament;

import com.gamearena.client.api.TournamentControllerApi;
import com.gamearena.client.model.ApiResponseListTournamentFullView;
import com.gamearena.client.model.ApiResponseListTournamentMemberView;
import com.gamearena.client.model.ApiResponseTournamentFullView;
import com.gamearena.client.model.ApiResponseTournamentMemberView;
import com.gamearena.client.model.PriceCreateRequest;
import com.gamearena.client.model.PriceEditRequest;
import com.gamearena.client.model.TournamentCreateRequest;
import com.gamearena.client.model.TournamentEditRequest;
import com.gamearena.client.model.TournamentMemberCreateRequest;
import com.gamearena.client.model.TournamentMemberEditRequest;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentCreateCereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberCreateCereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberEditCereal;
import hr.algebra.gamearena.webapp.service.ApiExceptionMapper;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberRoleDecereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberViewDecereal;
import hr.algebra.gamearena.webapp.service.authentication.user.IAuthenticatedUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentRestApiService implements ITournamentService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final TournamentControllerApi tournamentControllerApi;
    private final AuthenticatedApiClient<TournamentControllerApi> authenticatedTournamentClient;
    private final IAuthenticatedUserService authenticatedUserService;

    public TournamentRestApiService(
            TournamentControllerApi tournamentControllerApi,
            AuthenticatedApiClient<TournamentControllerApi> authenticatedTournamentClient,
            IAuthenticatedUserService authenticatedUserService)
    {
        this.tournamentControllerApi = tournamentControllerApi;
        this.authenticatedTournamentClient = authenticatedTournamentClient;
        this.authenticatedUserService = authenticatedUserService;
    }

    /** TOURNAMENT */
    // BEGIN
    @Override
    public List<TournamentFullViewDecereal> getAllTournaments() throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListTournamentFullView> response = tournamentControllerApi.getTournamentsWithHttpInfo();
            ApiResponseListTournamentFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch tournaments"));
            }

            return body.getData().stream()
                    .map(TournamentFullViewDecereal::fromTournamentFullViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public List<TournamentFullViewDecereal> getMyTournaments() throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException {
        TournamentControllerApi client;
        try {
            client = authenticatedTournamentClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseListTournamentFullView> response = client.getMyTournamentsWithHttpInfo();
            ApiResponseListTournamentFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch your tournaments"));
            }

            return body.getData().stream()
                    .map(TournamentFullViewDecereal::fromTournamentFullViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedOnly(ex);
        }
    }

    @Override
    public List<TournamentFullViewDecereal> getTournamentsByUserId(Long id) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListTournamentFullView> response = tournamentControllerApi.getTournamentsForUserWithHttpInfo(id);
            ApiResponseListTournamentFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch user's tournaments"));
            }

            return body.getData().stream()
                    .map(TournamentFullViewDecereal::fromTournamentFullViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public TournamentFullViewDecereal getTournamentById(Long id) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseTournamentFullView> response = tournamentControllerApi.getTournamentWithHttpInfo(id);
            ApiResponseTournamentFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch this tournament"));
            }

            return TournamentFullViewDecereal.fromTournamentFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public TournamentFullViewDecereal createTournament(TournamentCreateCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        TournamentControllerApi client;
        try {
            client = authenticatedTournamentClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to create a tournament"));
        }

        try {
            var price = new PriceCreateRequest()
                    .soloPrice(cereal.soloPrice())
                    .groupPrice(cereal.groupPrice())
                    .currency(cereal.currency().toPriceCreateClient());

            var request = new TournamentCreateRequest()
                    .name(cereal.name())
                    .description(cereal.description())
                    .gameId(cereal.gameId())
                    .price(price)
                    .startsAt(cereal.startsAt())
                    .endsAt(cereal.endsAt());

            ResponseEntity<ApiResponseTournamentFullView> response = client.createTournamentWithHttpInfo(request);
            ApiResponseTournamentFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return TournamentFullViewDecereal.fromTournamentFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedOrForbidden(ex);
        }
    }

    @Override
    public TournamentFullViewDecereal editTournament(Long tournamentId, TournamentEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        TournamentControllerApi client;
        try {
            client = authenticatedTournamentClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to edit a tournament"));
        }

        try {
            var price = new PriceEditRequest()
                    .soloPrice(cereal.soloPrice())
                    .groupPrice(cereal.groupPrice())
                    .currency(cereal.currency().toPriceEditClient());

            var request = new TournamentEditRequest()
                    .name(cereal.name())
                    .description(cereal.description())
                    .gameId(cereal.gameId())
                    .status(cereal.status().toClient())
                    .price(price)
                    .startsAt(cereal.startsAt())
                    .endsAt(cereal.endsAt());

            ResponseEntity<ApiResponseTournamentFullView> response = client.editTournamentWithHttpInfo(tournamentId, request);
            ApiResponseTournamentFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return TournamentFullViewDecereal.fromTournamentFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedForbiddenOrNotFound(ex);
        }
    }

    @Override
    public TournamentMemberViewDecereal addTournamentMember(Long tournamentId, TournamentMemberCreateCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, ConflictException, BadRequestedExceptions, UnexpectedApiErrorException {
        TournamentControllerApi client;
        try {
            client = authenticatedTournamentClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to add a tournament member"));
        }

        try {
            var request = new TournamentMemberCreateRequest().userId(cereal.userId()).role(cereal.role().toClient());
            ResponseEntity<ApiResponseTournamentMemberView> response = client.addTournamentMemberWithHttpInfo(tournamentId, request);
            ApiResponseTournamentMemberView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return TournamentMemberViewDecereal.fromTournamentMemberViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundForbiddenOrConflict(ex);
        }
    }

    @Override
    public TournamentMemberViewDecereal editTournamentMember(Long memberId, TournamentMemberEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, ConflictException, BadRequestedExceptions, UnexpectedApiErrorException {
        TournamentControllerApi client;
        try {
            client = authenticatedTournamentClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to edit a tournament member"));
        }

        try {
            var request = new TournamentMemberEditRequest().role(cereal.role().toClient());
            ResponseEntity<ApiResponseTournamentMemberView> response = client.editTournamentMemberWithHttpInfo(memberId, request);
            ApiResponseTournamentMemberView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return TournamentMemberViewDecereal.fromTournamentMemberViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundForbiddenOrConflict(ex);
        }
    }
    // END

    /** TOURNAMENT - TOURNAMENT MEMBER */
    // BEGIN
    @Override
    public List<TournamentFullViewDecereal> getOrganizersTournaments(Long callerId) {
        try {
            return getMyTournaments().stream()
                    .filter(tournament -> getMyTournamentMembershipOrEmpty(tournament.id())
                            .map(member -> member.role() == TournamentMemberRoleDecereal.ORGANIZER)
                            .orElse(false))
                    .toList();
        } catch (UnauthorizedException | NotFoundException | UnexpectedApiErrorException e) {
            return List.of();
        }
    }
    //END

    /** TOURNAMENT MEMBER */
    // BEGIN
    @Override
    public List<TournamentMemberViewDecereal> getTournamentMembers(Long tournamentId) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListTournamentMemberView> response = tournamentControllerApi.getMembersOfTournamentWithHttpInfo(tournamentId);
            ApiResponseListTournamentMemberView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch this tournament's members"));
            }

            return body.getData().stream().map(TournamentMemberViewDecereal::fromTournamentMemberViewClient).toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public TournamentMemberViewDecereal getMyTournamentMembership(Long tournamentId) throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException {
        TournamentControllerApi client;
        try {
            client = authenticatedTournamentClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseTournamentMemberView> response = client.getMeMemberOfTournamentWithHttpInfo(tournamentId);
            ApiResponseTournamentMemberView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch your tournament membership"));
            }

            return TournamentMemberViewDecereal.fromTournamentMemberViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedOrNotFound(ex);
        }
    }

    @Override
    public Optional<TournamentMemberViewDecereal> getMyTournamentMembershipOrEmpty(Long tournamentId) {
        if (!authenticatedUserService.isAuthenticated()) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(getMyTournamentMembership(tournamentId));
        } catch (UnauthorizedException | NotFoundException | UnexpectedApiErrorException e) {
            return Optional.empty();
        }
    }
    // END

    @Override
    public boolean isTournamentOrganizerOrAdmin(Long tournamentId) {
        if (authenticatedUserService.isAdmin()) {
            return true;
        }

        return getMyTournamentMembershipOrEmpty(tournamentId)
                .map(member -> member.role() == TournamentMemberRoleDecereal.ORGANIZER)
                .orElse(false);
    }
}
