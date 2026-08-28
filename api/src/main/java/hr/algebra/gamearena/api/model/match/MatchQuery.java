package hr.algebra.gamearena.api.model.match;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class MatchQuery{
    Optional<Long> gameId;
    Boolean ascending;
}
