package pba.models.replay.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.pokemon.Pokemon;
import pba.models.pokemon.Trainer;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Generation3ReplayData extends GenerationData {

  private Trainer trainerOne;
  private List<Pokemon> trainerOnePokemons = new ArrayList<>();
  private Trainer trainerTwo;
  private List<Pokemon> trainerTwoPokemons = new ArrayList<>();
  private boolean hasWinner;
  private LinkedList<Generation3Action> allActions = new LinkedList<>();
}
