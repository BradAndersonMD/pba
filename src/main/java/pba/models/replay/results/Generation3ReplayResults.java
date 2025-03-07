package pba.models.replay.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pba.models.Player;
import pba.models.Pokemon;
import pba.models.parser.generation.three.Generation3Action;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Generation3ReplayResults extends ReplayResults {

    private Player playerOne;
    private List<Pokemon> playerOnePokemons;
    private Player playerTwo;
    private List<Pokemon> playerTwoPokemons;

    private LinkedList<Generation3Action> allActions;

}
