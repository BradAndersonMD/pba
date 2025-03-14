package pba.models.replay.results;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pba.models.pokemon.PokemonData;
import pba.models.pokemon.Trainer;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Generation3Results extends ReplayResult {

  private List<PokemonData> pokemonData;
  private List<Trainer> trainers;
}
