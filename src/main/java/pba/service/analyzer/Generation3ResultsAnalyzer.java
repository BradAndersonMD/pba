package pba.service.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pba.models.Pokemon;
import pba.models.pokemon.PokemonData;
import pba.models.replay.data.Generation3ReplayData;
import pba.models.replay.results.Generation3Results;

@Slf4j
@RequiredArgsConstructor
public class Generation3ResultsAnalyzer
    implements ResultsAnalyzer<Generation3ReplayData, Generation3Results> {

  private final Map<String, PokemonData> trainAndPokemonToData = new HashMap<>();

  @Override
  public Generation3Results analyze(List<Generation3ReplayData> replayResults) {
    log.info("Analyzing [{}] replay results", replayResults.size());

    for (Generation3ReplayData data : replayResults) {
      parsePokemonRoster(data.getTrainerOnePokemons());
      parsePokemonRoster(data.getTrainerTwoPokemons());
    }

    List<PokemonData> pokemonData = trainAndPokemonToData.values().stream().toList();
    return new Generation3Results(pokemonData);
  }

  /**
   * Parses the roster for relevant information we want to capture
   *
   * @param pokemons the roster of Pokémon to capture information from
   */
  void parsePokemonRoster(List<Pokemon> pokemons) {

    for (Pokemon pokemon : pokemons) {
      String trainerAndPokemon = pokemon.getTrainer().getName() + " - " + pokemon.getName();
      PokemonData pokemonDataToUpdate =
          trainAndPokemonToData.getOrDefault(trainerAndPokemon, new PokemonData(trainerAndPokemon));
      pokemonDataToUpdate.update(pokemon);
      trainAndPokemonToData.put(trainerAndPokemon, pokemonDataToUpdate);
    }
  }
}
