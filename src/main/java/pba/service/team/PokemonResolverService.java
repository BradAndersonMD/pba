package pba.service.team;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import pba.models.pokemon.Pokemon;
import pba.models.pokemon.Trainer;

@AllArgsConstructor
public class PokemonResolverService {

  private final Map<String, List<Pokemon>> playerToPokemon;

  /**
   * Lookup a {@link Pokemon} by name and a particular {@link Trainer}
   *
   * @param trainer the player to lookup
   * @param name the name to lookup for the player
   * @return the found {@link Pokemon} or null if no Pokémon is present
   */
  public Pokemon resolve(Trainer trainer, String name) {
    return playerToPokemon.get(trainer.getShortHandName()).stream()
        .filter(
            pokemon ->
                pokemon.getName().equalsIgnoreCase(name)
                    || pokemon.getNickname().equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);
  }
}
