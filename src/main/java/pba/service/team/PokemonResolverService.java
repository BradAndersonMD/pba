package pba.service.team;

import lombok.RequiredArgsConstructor;
import pba.models.pokemon.Pokemon;
import pba.models.pokemon.Trainer;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PokemonResolverService {

  private final Map<String, List<Pokemon>> playerToPokemon;

  /**
   * Lookup a {@link Pokemon} by pokemonName and a particular {@link Trainer}
   *
   * @param trainer the trainer to lookup
   * @param pokemonName the name for a Pokémon to lookup for the trainer
   * @return the found {@link Pokemon} or null if no Pokémon is present
   */
  public Pokemon resolve(Trainer trainer, String pokemonName) {
    return playerToPokemon.get(trainer.getShortHandName()).stream()
        .filter(
            pokemon ->
                    pokemonName.equalsIgnoreCase(pokemon.getName())
                    || pokemonName.equalsIgnoreCase(pokemon.getNickname()))
        .findFirst()
        .orElse(null);
  }
}
