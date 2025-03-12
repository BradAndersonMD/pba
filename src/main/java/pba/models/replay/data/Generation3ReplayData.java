package pba.models.replay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pba.models.Pokemon;
import pba.models.Trainer;
import pba.models.parser.generation.three.Generation3Action;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Generation3ReplayData extends GenerationData {

  private Trainer trainerOne;
  private List<Pokemon> trainerOnePokemons;
  private Trainer trainerTwo;
  private List<Pokemon> trainerTwoPokemons;
  private LinkedList<Generation3Action> allActions;
}
