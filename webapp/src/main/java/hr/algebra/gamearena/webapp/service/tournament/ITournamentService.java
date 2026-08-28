package hr.algebra.gamearena.webapp.service.tournament;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentCreateCereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberCreateCereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberViewDecereal;

import java.util.List;
import java.util.Optional;

public interface ITournamentService {

    /** Tournament */
    List<TournamentFullViewDecereal> getAllTournaments() throws NotFoundException, UnexpectedApiErrorException;
    List<TournamentFullViewDecereal> getMyTournaments() throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException;
    List<TournamentFullViewDecereal> getTournamentsByUserId(Long id) throws NotFoundException, UnexpectedApiErrorException;
    TournamentFullViewDecereal getTournamentById(Long id) throws NotFoundException, UnexpectedApiErrorException;
    TournamentFullViewDecereal createTournament(TournamentCreateCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;
    TournamentFullViewDecereal editTournament(Long tournamentId, TournamentEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;

    /** Tournament - Tournament members*/
    List<TournamentFullViewDecereal> getOrganizersTournaments(Long callerId);

    /** Tournament members */
    List<TournamentMemberViewDecereal> getTournamentMembers(Long tournamentId) throws NotFoundException, UnexpectedApiErrorException;
    TournamentMemberViewDecereal getMyTournamentMembership(Long tournamentId) throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException;
    Optional<TournamentMemberViewDecereal> getMyTournamentMembershipOrEmpty(Long tournamentId);
    TournamentMemberViewDecereal addTournamentMember(Long tournamentId, TournamentMemberCreateCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, ConflictException, BadRequestedExceptions, UnexpectedApiErrorException;
    TournamentMemberViewDecereal editTournamentMember(Long memberId, TournamentMemberEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, ConflictException, BadRequestedExceptions, UnexpectedApiErrorException;
}
