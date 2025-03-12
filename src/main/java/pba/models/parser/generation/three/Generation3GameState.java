package pba.models.parser.generation.three;

import lombok.Getter;
import lombok.Setter;
import pba.models.Pair;
import pba.models.Pokemon;
import pba.models.parser.generation.GameState;

/** Tracks global state across {@link Generation3Action}'s. */
@Getter
@Setter
public class Generation3GameState extends GameState {

  private Pair<Weather, Pokemon> weatherToPokemon;
}
