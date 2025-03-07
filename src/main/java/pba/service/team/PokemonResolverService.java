package pba.service.team;

import lombok.AllArgsConstructor;
import pba.models.Player;
import pba.models.Pokemon;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class PokemonResolverService {

    private final Map<String, List<Pokemon>> playerToPokemon;

    /**
     * Lookup a {@link Pokemon} by name and a particular {@link Player}
     * @param player the player to lookup
     * @param name the name to lookup for the player
     * @return the found {@link Pokemon}
     */
    public Optional<Pokemon> resolve(Player player, String name) {
        return playerToPokemon.get(player.getShortHandName()).stream()
                .filter(pokemon -> pokemon.getName().equals(name) || pokemon.getNickname().equals(name))
                .findFirst();

    }

}
