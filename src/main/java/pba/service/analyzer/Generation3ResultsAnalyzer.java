package pba.service.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pba.models.pokemon.Pokemon;
import pba.models.replay.data.Generation3ReplayData;
import pba.models.replay.results.Generation3Results;
import pba.models.replay.results.ResultsData;

@Slf4j
@RequiredArgsConstructor
public class Generation3ResultsAnalyzer
    implements ResultsAnalyzer<Generation3ReplayData, Generation3Results> {

  private final Map<String, ResultsData> trainAndPokemonToData = new HashMap<>();

  @Override
  public Generation3Results analyze(List<Generation3ReplayData> replayResults) {
    log.info("Analyzing [{}] replay results", replayResults.size());

    for (Generation3ReplayData data : replayResults) {
      parsePokemonRoster(data.getTrainerOnePokemons());
      parsePokemonRoster(data.getTrainerTwoPokemons());
    }

    // Calculate final per game stats
    List<ResultsData> resultsData =
        trainAndPokemonToData.values().stream().map(ResultsData::calculatePerGameStats).toList();
    return new Generation3Results(resultsData);
  }

  /**
   * Parses the roster for relevant information we want to capture
   *
   * @param pokemons the roster of Pokémon to capture information from
   */
  void parsePokemonRoster(List<Pokemon> pokemons) {

    for (Pokemon pokemon : pokemons) {
      String trainerAndPokemon = pokemon.getTrainer().getName() + " - " + pokemon.getName();
      ResultsData resultsDataToUpdate =
          trainAndPokemonToData.getOrDefault(trainerAndPokemon, new ResultsData(pokemon));
      resultsDataToUpdate.update(pokemon);
      trainAndPokemonToData.put(trainerAndPokemon, resultsDataToUpdate);
    }
  }
}
