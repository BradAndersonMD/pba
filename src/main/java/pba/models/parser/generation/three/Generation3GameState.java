package pba.models.parser.generation.three;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import pba.models.Pair;
import pba.models.parser.generation.GameState;
import pba.models.pokemon.Pokemon;

/** Tracks global state across {@link Generation3Action}'s. */
@Getter
@Setter
@Generated
public class Generation3GameState extends GameState {

  private Pair<Hazard, Pokemon> hazardToPokemon;
  private Pair<Weather, Pokemon> weatherToPokemon;
}
