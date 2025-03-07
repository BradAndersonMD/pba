package pba.models.replay.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pba.models.Trainer;
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

    private Trainer trainerOne;
    private List<Pokemon> trainerOnePokemons;
    private Trainer trainerTwo;
    private List<Pokemon> trainerTwoPokemons;
    private int turnCount = 0;

    private LinkedList<Generation3Action> allActions;

    public void incrementTurnCount() {
        turnCount++;
    }

}
