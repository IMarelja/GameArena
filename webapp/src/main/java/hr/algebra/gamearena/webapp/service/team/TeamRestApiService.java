package hr.algebra.gamearena.webapp.service.team;

import com.gamearena.client.api.TeamControllerApi;
import com.gamearena.client.model.ApiResponseListTeamMemberMinimalView;
import com.gamearena.client.model.ApiResponseListTeamMinimalView;
import com.gamearena.client.model.ApiResponseTeamMemberFullView;
import com.gamearena.client.model.ApiResponseTeamMinimalView;
import com.gamearena.client.model.TeamEditRequest;
import com.gamearena.client.model.TeamMemberEditRequest;
import com.gamearena.client.model.TeamCreateRequest;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamAddCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;
import hr.algebra.gamearena.webapp.service.ApiExceptionMapper;
import hr.algebra.gamearena.webapp.security.AuthenticatedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class TeamRestApiService implements ITeamService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final TeamControllerApi teamControllerApi;
    private final AuthenticatedApiClient<TeamControllerApi> authenticatedTeamClient;

    public TeamRestApiService(TeamControllerApi teamControllerApi, AuthenticatedApiClient<TeamControllerApi> authenticatedTeamClient) {
        this.teamControllerApi = teamControllerApi;
        this.authenticatedTeamClient = authenticatedTeamClient;
    }

    @Override
    public List<TeamMinimalViewDecereal> getAllTeams() throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListTeamMinimalView> response = teamControllerApi.getAllTeamsWithHttpInfo();
            ApiResponseListTeamMinimalView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch teams"));
            }

            return body.getData().stream().map(TeamMinimalViewDecereal::fromTeamMinimalViewClient).toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public TeamMinimalViewDecereal getTeamById(Long id) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseTeamMinimalView> response = teamControllerApi.getTeamByIdWithHttpInfo(id);
            ApiResponseTeamMinimalView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch this team"));
            }

            return TeamMinimalViewDecereal.fromTeamMinimalViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public TeamMinimalViewDecereal addTeam(TeamAddCereal cereal) throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException, ForbiddenException, BadRequestedExceptions {
        TeamControllerApi client;
        try {
            client = authenticatedTeamClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to edit a team"));
        }

        try{
            var request = new TeamCreateRequest().gameId(cereal.gameId()).name(cereal.name());
            ResponseEntity<ApiResponseTeamMinimalView> response = client.createTeamWithHttpInfo(request);
            ApiResponseTeamMinimalView body = response.getBody();

            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return TeamMinimalViewDecereal.fromTeamMinimalViewClient(body.getData());

        }catch (RestClientResponseException ex){
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundOrForbidden(ex);
        }

    }

    @Override
    public TeamMinimalViewDecereal editTeam(Long id, TeamEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        TeamControllerApi client;
        try {
            client = authenticatedTeamClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to edit a team"));
        }

        try {
            var request = new TeamEditRequest().name(cereal.name()).gameId(cereal.gameId());
            ResponseEntity<ApiResponseTeamMinimalView> response = client.editTeamWithHttpInfo(id, request);
            ApiResponseTeamMinimalView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return TeamMinimalViewDecereal.fromTeamMinimalViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedForbiddenOrNotFound(ex);
        }
    }

    @Override
    public List<TeamMemberMinimalViewDecereal> getTeamMembers(Long teamId) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListTeamMemberMinimalView> response = teamControllerApi.getTeamMembersWithHttpInfo(teamId);
            ApiResponseListTeamMemberMinimalView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch this team's members"));
            }

            return body.getData().stream().map(TeamMemberMinimalViewDecereal::fromTeamMemberMinimalViewClient).toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public TeamMemberFullViewDecereal getMyTeamMembership(Long teamId) throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException {
        TeamControllerApi client;
        try {
            client = authenticatedTeamClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseTeamMemberFullView> response = client.getMeTeamMemberWithHttpInfo(teamId);
            ApiResponseTeamMemberFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch your team membership"));
            }

            return TeamMemberFullViewDecereal.fromTeamMemberFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedOrNotFound(ex);
        }
    }

    @Override
    public Optional<TeamMemberFullViewDecereal> getMyTeamMembershipOrEmpty(Long teamId) {
        if (!AuthenticatedUser.isAuthenticated()) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(getMyTeamMembership(teamId));
        } catch (UnauthorizedException | NotFoundException | UnexpectedApiErrorException e) {
            return Optional.empty();
        }
    }

    @Override
    public TeamMemberFullViewDecereal editTeamMemberRole(Long teamId, Long memberId, TeamMemberEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        TeamControllerApi client;
        try {
            client = authenticatedTeamClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to edit team members"));
        }

        try {
            var request = new TeamMemberEditRequest().role(cereal.role().toClient());
            ResponseEntity<ApiResponseTeamMemberFullView> response = client.editTeamMemberRoleWithHttpInfo(teamId, memberId, request);
            ApiResponseTeamMemberFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return TeamMemberFullViewDecereal.fromTeamMemberFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedForbiddenOrNotFound(ex);
        }
    }

    @Override
    public void removeTeamMember(Long teamId, Long memberId) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        TeamControllerApi client;
        try {
            client = authenticatedTeamClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to remove team members"));
        }

        try {
            client.removeTeamMember(teamId, memberId);
        } catch (RestClientResponseException ex) {
            ApiExceptionMapper.unauthorizedForbiddenOrNotFound(ex);
        }
    }
}
