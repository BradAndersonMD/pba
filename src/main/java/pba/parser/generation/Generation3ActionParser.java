package pba.parser.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import pba.models.Pair;
import pba.models.pokemon.Pokemon;
import pba.models.parser.generation.three.StatusEffect;
import pba.models.pokemon.Trainer;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.parser.generation.three.Generation3GameState;
import pba.models.parser.generation.three.Hazard;
import pba.models.parser.generation.three.Weather;
import pba.models.replay.data.Generation3ReplayData;
import pba.service.team.PokemonResolverService;

@Slf4j
public class Generation3ActionParser implements GenerationActionParser<Generation3ReplayData> {

  private final PokemonResolverService pokemonResolverService;
  private final Map<String, List<Pokemon>> trainerToPokemon;
  private final Generation3GameState gameState;
  // Tracks all actions so far - useful for back tracking
  private final LinkedList<Generation3Action> allActions = new LinkedList<>();
  private final Generation3ReplayData replayResultsData = new Generation3ReplayData();

  public Generation3ActionParser() {
    this.trainerToPokemon = new HashMap<>();
    this.gameState = new Generation3GameState();
    this.pokemonResolverService = new PokemonResolverService(trainerToPokemon);
  }

  @Override
  public Generation3ReplayData parseLines(String[] lines) {

    for (String line : lines) {
      if(replayResultsData.isHasWinner()) {
        break;
      }
      Generation3Action action = new Generation3Action(line);
      log.debug("ActionType=[{}], Value=[{}]", action.getActionType(), action.getValue());
      if (action.isEligibleAction()) {
        applyAction(action);
      }

    }

    replayResultsData.setAllActions(allActions);
    replayResultsData.setTrainerOnePokemons(trainerToPokemon.get("p1"));
    replayResultsData.setTrainerTwoPokemons(trainerToPokemon.get("p2"));
    return replayResultsData;
  }

  /**
   * Side effects are applied to the {@link Generation3ReplayData}
   *
   * @param action the action to apply
   */
  private void applyAction(Generation3Action action) {
    allActions.add(action);

    String currentAction = action.getActionType();
    String value = action.getValue();
    String[] valueSplit = value.split("\\|");

    // Capture Trainer name + shorthand
    if (currentAction.contains("|player|")) {
      String shortHandName = valueSplit[0]; // p1
      String trainerName = valueSplit[1]; //  SomeTrainerNickname
      addTrainerToGameState(new Trainer(trainerName, shortHandName));
      return;
    }

    // Capture Pokémon swapping into battlefield and its current health
    // Value contains, handler shortname + Pokémon nickname, Pokémon name + possible gender,
    // and their current health
    // example = p1a: NickName|PokemonName + Gender(?)|100/100
    if (currentAction.contains("|switch|") || currentAction.contains("|drag|")) {
      // Split into resolved Trainer and Pokémon - "p1a: NickName"
      Pair<Trainer, String> trainerAndPokemonNickName = retrieveTrainerAndPokemonNickName(value);
      Trainer trainer = trainerAndPokemonNickName.key(); // Trainer
      String pokemonNickName = trainerAndPokemonNickName.value(); // Pokemon nickname

      // If we can't resolve this Pokémon this means we need to create it.
      Optional<Pokemon> possiblePokemon =
          Optional.ofNullable(pokemonResolverService.resolve(trainer, pokemonNickName));
      if (possiblePokemon.isEmpty()) {
        // Work around gendered and non-gendered pokemon
        String pokemonNameAndPossibleGender = valueSplit[1];
        String pokemonName =
            pokemonNameAndPossibleGender.contains(",")
                ? pokemonNameAndPossibleGender.substring(
                    0, pokemonNameAndPossibleGender.indexOf(","))
                : pokemonNameAndPossibleGender;

        Pokemon pokemon = new Pokemon();
        pokemon.setTrainer(trainer);
        pokemon.setName(pokemonName.trim());
        pokemon.setNickname(pokemonNickName.trim());
        trainerToPokemon.get(trainer.getShortHandName()).add(pokemon);
      }
      return;
    }

    if (currentAction.contains("|-damage|")) {
      Pair<Trainer, Pokemon> trainerAndPokemon = retrieveTrainerAndResolvedPokemon(value);
      Trainer trainer = trainerAndPokemon.key();
      Pokemon pokemon = trainerAndPokemon.value();

      if (value.contains("0 fnt")) {

        Generation3Action previousMoveAction = getPreviousActionUntil("|move|");
        String previousValue = previousMoveAction.getValue().split("\\|")[0];

        Pair<Trainer, Pokemon> previousTrainerAndPokemon =
            retrieveTrainerAndResolvedPokemon(previousValue);
        Trainer previousTrainer = previousTrainerAndPokemon.key();
        Pokemon previousPokemon = previousTrainerAndPokemon.value();

        // Deal remaining HP
        previousPokemon.dealDamage(pokemon.getCurrentHealth());
        previousPokemon.incrementKnockout();
        pokemon.takeDamage(pokemon.getCurrentHealth());

        updatePokemon(previousTrainer, previousPokemon);
        updatePokemon(trainer, pokemon);
        return;
      }

      int health = Integer.parseInt(value.substring(value.indexOf("|") + 1, value.indexOf("/")));
      int damageDealt = pokemon.getCurrentHealth() - health;

      if (!value.contains("[from]") && !value.contains("[of]")) {
        Generation3Action previousMoveAction = getPreviousActionUntil("|move|");
        String previousValue = previousMoveAction.getValue().split("\\|")[0];

        Pair<Trainer, Pokemon> previousTrainerAndPokemon =
            retrieveTrainerAndResolvedPokemon(previousValue);
        Trainer previousTrainer = previousTrainerAndPokemon.key();
        Pokemon previousPokemon = previousTrainerAndPokemon.value();

        previousPokemon.dealDamage(damageDealt);
        pokemon.takeDamage(damageDealt);

        updatePokemon(previousTrainer, previousPokemon);
        updatePokemon(trainer, pokemon);
        return;
      }
      // This might wrongly attribute who did what damage
      // i.e. leech seed or curse
      if (value.contains("[from]") && value.contains("[of]")) {
        String enemyPokemonValue = valueSplit[3];

        Pair<Trainer, Pokemon> previousTrainerAndPokemon =
            retrieveTrainerAndResolvedPokemon(enemyPokemonValue);
        Trainer enemyTrainer = previousTrainerAndPokemon.key();
        Pokemon enemyPokemon = previousTrainerAndPokemon.value();

        enemyPokemon.dealDamage(damageDealt);
        pokemon.takeDamage(damageDealt);

        updatePokemon(enemyTrainer, enemyPokemon);
        updatePokemon(trainer, pokemon);
        return;
      }

      // Weather/Hazard/Status Effects
      String parsed = valueSplit[2].substring(6).trim();

      // Weather damage
      Weather foundWeather = Weather.get(parsed);
      if (foundWeather != null) {
        Pokemon weatherPokemon = gameState.getWeatherToPokemon().value();
        Trainer weatherPokemonTrainer = weatherPokemon.getTrainer();

        weatherPokemon.dealDamage(damageDealt);
        pokemon.takeDamage(damageDealt);

        updatePokemon(trainer, pokemon);
        updatePokemon(weatherPokemonTrainer, weatherPokemon);
        return;
      }

      Hazard foundHazard = Hazard.get(parsed);
      if (foundHazard != null) {
        Pokemon hazardPokemon = gameState.getHazardToPokemon().value();
        Trainer hazardPokemonTrainer = hazardPokemon.getTrainer();

        hazardPokemon.dealDamage(damageDealt);
        pokemon.takeDamage(damageDealt);

        updatePokemon(trainer, pokemon);
        updatePokemon(hazardPokemonTrainer, hazardPokemon);
        return;
      }

      // Status effect damage
      StatusEffect statusEffect = StatusEffect.get(parsed);
      if (statusEffect != null) {
        Pair<StatusEffect, Pokemon> statusEffectAndPokemon = pokemon.getStatusEffectAndPokemon();
        Pokemon statusEffectPokemon = statusEffectAndPokemon.value();
        Trainer statusEffectPokemonTrainer = statusEffectPokemon.getTrainer();

        statusEffectPokemon.dealDamage(damageDealt);
        pokemon.takeDamage(damageDealt);

        updatePokemon(trainer, pokemon);
        updatePokemon(statusEffectPokemonTrainer, statusEffectPokemon);
        return;
      }
    }

    if (currentAction.contains("|move|")) {

      Hazard hazard = Hazard.get(valueSplit[1]);
      if (hazard != null) {
        Pair<Trainer, Pokemon> hazardTrainerAndPokemon =
            retrieveTrainerAndResolvedPokemon(valueSplit[0]);

        Trainer hazardTrainer = hazardTrainerAndPokemon.key();
        Pokemon hazardPokemon = hazardTrainerAndPokemon.value();
        gameState.setHazardToPokemon(new Pair<>(hazard, hazardPokemon));
        updatePokemon(hazardTrainer, hazardPokemon);
      }

      return;
    }

    if (currentAction.contains("|-sideend|") && value.contains("[from]")) {
      Hazard hazard = Hazard.get(valueSplit[1]);
      if (gameState.getHazardToPokemon().key().equals(hazard)) {
        gameState.setHazardToPokemon(null);
      }
    }

    if (currentAction.contains("|-weather") && value.contains("[of]")) {
      Pair<Trainer, Pokemon> hazardTrainerAndPokemon =
          retrieveTrainerAndResolvedPokemon(valueSplit[2].substring(5));
      Trainer hazardTrainer = hazardTrainerAndPokemon.key();
      Pokemon hazardPokemon = hazardTrainerAndPokemon.value();

      gameState.setWeatherToPokemon(new Pair<>(Weather.get(valueSplit[0]), hazardPokemon));
      String unparsedAbility = valueSplit[1];
      String ability = unparsedAbility.substring((unparsedAbility.indexOf(":") + 1)).trim();

      hazardPokemon.setAbility(ability);
      updatePokemon(hazardTrainer, hazardPokemon);
      return;
    }

    if (currentAction.contains("|-status|")) {
      Pair<Trainer, Pokemon> trainerAndPokemon = retrieveTrainerAndResolvedPokemon(value);
      Trainer trainer = trainerAndPokemon.key();
      Pokemon pokemon = trainerAndPokemon.value();

      Generation3Action previousMoveAction = getPreviousActionUntil("|move|");
      String previousValue = previousMoveAction.getValue().split("\\|")[0];

      Pair<Trainer, Pokemon> previousTrainerAndPokemon =
          retrieveTrainerAndResolvedPokemon(previousValue);
      Pokemon previousPokemon = previousTrainerAndPokemon.value();

      pokemon.setStatusEffectAndPokemon(
          new Pair<>(StatusEffect.get(valueSplit[1]), previousPokemon));
      updatePokemon(trainer, pokemon);
      return;
    }

    if (currentAction.contains("|-heal|")) {
      Pair<Trainer, Pokemon> trainerAndPokemon = retrieveTrainerAndResolvedPokemon(value);
      Trainer trainer = trainerAndPokemon.key();
      Pokemon pokemon = trainerAndPokemon.value();

      String unparsedHealth = value.split("\\|")[1];
      int health = Integer.parseInt(unparsedHealth.substring(0, unparsedHealth.indexOf("/")));
      pokemon.setCurrentHealth(health);
      updatePokemon(trainer, pokemon);
      return;
    }

    if(currentAction.contains("|faint|")) {
      Pair<Trainer, Pokemon> trainerAndPokemon = retrieveTrainerAndResolvedPokemon(value);
      Trainer trainer = trainerAndPokemon.key();
      Pokemon pokemon = trainerAndPokemon.value();

      pokemon.setCurrentHealth(0);
      updatePokemon(trainer, pokemon);
    }

    if (currentAction.contains("|win|")) {
      replayResultsData.setHasWinner(true);
    }

  }

  /**
   * Adds {@link Trainer} one or two into the game state mapping
   *
   * @param trainer the trainer to add
   */
  private void addTrainerToGameState(Trainer trainer) {
    if (replayResultsData.getTrainerOne() == null) {
      replayResultsData.setTrainerOne(trainer);
    } else {
      replayResultsData.setTrainerTwo(trainer);
    }
    trainerToPokemon.put(trainer.getShortHandName(), new ArrayList<>());
  }

  /**
   * Splits a value into a {@link Trainer} and Pokémon nick name {@link Pair}
   *
   * @param value the value to split and parse
   * @return a {@link Pair} of {@link Trainer} and {@link Pokemon}
   */
  private Pair<Trainer, String> retrieveTrainerAndPokemonNickName(String value) {
    // Split shorthand name and Pokémon nickname
    String[] splitValue = value.split("\\|")[0].split(":"); // ["p1a", "NickName"]
    Trainer trainer = retrieveTrainerForShorthand(splitValue[0].substring(0, 2).trim());
    return new Pair<>(trainer, splitValue[1].trim());
  }

  /**
   * Splits a value into a {@link Trainer} and Pokémon nick name {@link Pair}
   *
   * @param value the value to split and parse
   * @return a {@link Pair} of {@link Trainer} and {@link Pokemon}
   */
  private Pair<Trainer, Pokemon> retrieveTrainerAndResolvedPokemon(String value) {
    // Split shorthand name and Pokémon nickname
    String[] splitValue = value.split("\\|")[0].split(":"); // ["p1a", "NickName"]
    String playerShortHand = splitValue[0].contains("p1") ? "p1" : "p2";
    Trainer trainer = retrieveTrainerForShorthand(playerShortHand.trim());
    Pokemon pokemon = pokemonResolverService.resolve(trainer, splitValue[1].trim());
    return new Pair<>(trainer, pokemon);
  }

  /**
   * Retrieves the {@link Trainer} for a given shorthand name
   *
   * @param shorthand the shorthand name to resolve
   * @return the {@link Trainer} with the matching shorthand name
   */
  private Trainer retrieveTrainerForShorthand(String shorthand) {
    return shorthand.contains(replayResultsData.getTrainerOne().getShortHandName())
        ? replayResultsData.getTrainerOne()
        : replayResultsData.getTrainerTwo();
  }

  private void updatePokemon(Trainer trainer, Pokemon pokemonToUpdate) {
    List<Pokemon> pokemons = trainerToPokemon.get(trainer.getShortHandName());
    Optional<Pokemon> possiblePokemon =
        pokemons.stream()
            .filter(pokemon -> pokemon.getUuid().equals(pokemonToUpdate.getUuid()))
            .findFirst();
    possiblePokemon.ifPresent(pokemon -> pokemon.update(pokemonToUpdate));
  }

  private Generation3Action getPreviousActionUntil(String actionTypeLookup) {
    ListIterator<Generation3Action> generation3ActionListIterator =
        allActions.listIterator(allActions.size());
    Generation3Action previous = null;
    while (generation3ActionListIterator.hasPrevious()) {
      previous = generation3ActionListIterator.previous();
      if (previous.getActionType().equals(actionTypeLookup)) {
        break;
      }
    }

    return previous;
  }
}
