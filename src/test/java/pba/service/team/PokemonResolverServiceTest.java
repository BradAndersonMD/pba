package pba.service.team;

import org.junit.jupiter.api.Test;
import pba.models.pokemon.Pokemon;
import pba.models.pokemon.Trainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonResolverServiceTest {

    @Test
    void itShouldResolve_ByName(){
        Map<String, List<Pokemon>> playerToPokemon = new HashMap<>();
        Pokemon expected = new Pokemon();
        List<Pokemon> roster = new ArrayList<>();

        expected.setName("Charizard");
        roster.add(expected);
        playerToPokemon.put("p1", roster);

        PokemonResolverService pokemonResolverService = new PokemonResolverService(playerToPokemon);
        Trainer trainer = new Trainer("Brad", "p1");
        Pokemon actual = pokemonResolverService.resolve(trainer, "Charizard");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void itShouldResolve_ByNickName(){
        Map<String, List<Pokemon>> playerToPokemon = new HashMap<>();
        Pokemon expected = new Pokemon();
        List<Pokemon> roster = new ArrayList<>();

        expected.setNickname("charchar");
        roster.add(expected);
        playerToPokemon.put("p1", roster);
        PokemonResolverService pokemonResolverService = new PokemonResolverService(playerToPokemon);

        Trainer trainer = new Trainer("Brad", "p1");
        Pokemon actual = pokemonResolverService.resolve(trainer, "charchar");

        assertThat(actual).isEqualTo(expected);
    }
}
