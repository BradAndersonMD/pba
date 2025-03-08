package pba.parser.generation;

import pba.models.Pair;
import pba.models.StatusEffect;
import pba.models.Trainer;
import pba.models.Pokemon;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.parser.generation.three.Generation3GameState;
import pba.models.parser.generation.three.Weather;
import pba.models.replay.results.Generation3ReplayResults;
import pba.service.team.PokemonResolverService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

public class Generation3ActionParser implements GenerationActionParser<Generation3Action, Generation3ReplayResults> {

    private final Map<String, List<Pokemon>> trainerToPokemon;
    private final PokemonResolverService pokemonResolverService;
    private final Generation3ReplayResults results = new Generation3ReplayResults();
    // Tracks all actions so far - useful for back tracking
    private final LinkedList<Generation3Action> allActions = new LinkedList<>();
    private final Generation3GameState gameState = new Generation3GameState();

    public Generation3ActionParser() {
        this.trainerToPokemon = new HashMap<>();
        this.pokemonResolverService = new PokemonResolverService(trainerToPokemon);
    }

    @Override
    public Generation3ReplayResults retrieveResults() {
        results.setAllActions(allActions);
        results.setTrainerOnePokemons(trainerToPokemon.get("p1"));
        results.setTrainerTwoPokemons(trainerToPokemon.get("p2"));
        return results;
    }

    @Override
    public void applyAction(Generation3Action action) {
        allActions.add(action);
        if(action.isInactionable()){
            return;
        }

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
        if (currentAction.contains("|switch|")) {
            // Split into resolved Trainer and Pokémon - "p1a: NickName"
            Pair<Trainer, String> trainerAndPokemonNickName = retrieveTrainerAndPokemonNickName(value);
            Trainer trainer = trainerAndPokemonNickName.key(); // Trainer
            String pokemonNickName = trainerAndPokemonNickName.value(); // Pokemon nickname

            // If we can't resolve this Pokémon this means we need to create it.
            Optional<Pokemon> possiblePokemon = Optional.ofNullable(pokemonResolverService.resolve(trainer, pokemonNickName));
            if(possiblePokemon.isEmpty()) {
                // Some Pokémon don't have genders, but if they do its captured here.
                // We need to hand both cases:
                // Jynx, F
                // Zapdos
                String pokemonNameAndPossibleGender = valueSplit[1];
                String pokemonName = pokemonNameAndPossibleGender.contains(",")
                        ? pokemonNameAndPossibleGender.substring(0, pokemonNameAndPossibleGender.indexOf(","))
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

            int health = Integer.parseInt(value.substring(value.indexOf("|") + 1, value.indexOf("/")));
            int damageDealt = pokemon.getCurrentHealth() - health;

            // Weather/StatusEffect damage
            if(value.contains("[from]")) {

                String parsed = valueSplit[2].substring(6).trim();

                // Attribute to correct Pokémon
                // Trying converting to weather
                Weather foundWeather = Weather.get(parsed);
                if(foundWeather != null){
                    Pokemon weatherPokemon = gameState.getWeatherToPokemon().value();
                    Trainer weatherPokemonTrainer = weatherPokemon.getTrainer();

                    weatherPokemon.dealDamage(damageDealt);
                    updatePokemon(weatherPokemonTrainer, weatherPokemon);
                }
                else {
                    // Must be a status effect
                    Pair<StatusEffect, Pokemon> statusEffectAndPokemon = pokemon.getStatusEffectAndPokemon();
                    Pokemon statusEffectPokemon = statusEffectAndPokemon.value();
                    Trainer statusEffectPokemonTrainer = statusEffectPokemon.getTrainer();

                    statusEffectPokemon.dealDamage(damageDealt);
                    updatePokemon(statusEffectPokemonTrainer, statusEffectPokemon);
                }
                pokemon.takeDamage(damageDealt);
            } else {

                Generation3Action previousMoveAction = getPreviousActionUntil("|move|");
                String previousValue = previousMoveAction.getValue().split("\\|")[0];

                Pair<Trainer, Pokemon> previousTrainerAndPokemon = retrieveTrainerAndResolvedPokemon(previousValue);
                Trainer previousTrainer = previousTrainerAndPokemon.key();
                Pokemon previousPokemon = previousTrainerAndPokemon.value();

                previousPokemon.dealDamage(damageDealt);
                pokemon.takeDamage(damageDealt);

                updatePokemon(previousTrainer, previousPokemon);
            }

            updatePokemon(trainer, pokemon);

            return;
        }

        if (currentAction.contains("|-weather") && value.contains("[of]")) {
            Pair<Trainer, Pokemon> weatherTrainerAndPokemon = retrieveTrainerAndResolvedPokemon(valueSplit[2].substring(5));
            Trainer weatherTrainer = weatherTrainerAndPokemon.key();
            Pokemon weatherPokemon = weatherTrainerAndPokemon.value();

            gameState.setWeatherToPokemon(new Pair<>(Weather.get(valueSplit[0]), weatherPokemon));
            String unparsedAbility = valueSplit[1];
            String ability = unparsedAbility.substring((unparsedAbility.indexOf(":") + 1)).trim();

            weatherPokemon.setAbility(ability);
            updatePokemon(weatherTrainer, weatherPokemon);
            return;
        }

        if (currentAction.contains("|-status|")) {
            Pair<Trainer, Pokemon> trainerAndPokemon = retrieveTrainerAndResolvedPokemon(value);
            Trainer trainer = trainerAndPokemon.key();
            Pokemon pokemon = trainerAndPokemon.value();

            Generation3Action previousMoveAction = getPreviousActionUntil("|move|");
            String previousValue = previousMoveAction.getValue().split("\\|")[0];

            Pair<Trainer, Pokemon> previousTrainerAndPokemon = retrieveTrainerAndResolvedPokemon(previousValue);
            Pokemon previousPokemon = previousTrainerAndPokemon.value();

            pokemon.setStatusEffectAndPokemon(new Pair<>(StatusEffect.get(valueSplit[1]), previousPokemon));
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

        if (currentAction.contains("|faint|")) {
            Pair<Trainer, Pokemon> trainerAndPokemon = retrieveTrainerAndResolvedPokemon(value);
            Trainer trainer = trainerAndPokemon.key();
            Pokemon pokemon = trainerAndPokemon.value();

            pokemon.setTotalHealth(0);
            updatePokemon(trainer, pokemon);
            return;
        }

        if (currentAction.contains("|win|")) {
            Trainer winner = value.contains(results.getTrainerOne().getName())
                    ? results.getTrainerOne()
                    : results.getTrainerTwo();
            results.setWinner(winner);
        }

    }

    /**
     * Adds {@link Trainer} one or two into the game state mapping
     * @param trainer the trainer to add
     */
    private void addTrainerToGameState(Trainer trainer) {
        if (results.getTrainerOne() == null){
            results.setTrainerOne(trainer);
        } else {
            results.setTrainerTwo(trainer);
        }
        trainerToPokemon.put(trainer.getShortHandName(), new ArrayList<>());
    }

    /**
     * Splits a value into a {@link Trainer} and Pokémon nick name {@link Pair}
     * @param value the value to split and parse
     * @return a {@link Pair} of {@link Trainer} and {@link Pokemon}
     */
    private Pair<Trainer, String> retrieveTrainerAndPokemonNickName(String value) {
        // Split shorthand name and Pokémon nickname
        String[] splitValue = value.split("\\|")[0].split(":"); // ["p1a", "NickName"]
        Trainer trainer = retrieveTrainerForShorthand(splitValue[0].substring(0, 2));
        return new Pair<>(trainer,  splitValue[1]);
    }

    /**
     * Splits a value into a {@link Trainer} and Pokémon nick name {@link Pair}
     * @param value the value to split and parse
     * @return a {@link Pair} of {@link Trainer} and {@link Pokemon}
     */
    private Pair<Trainer, Pokemon> retrieveTrainerAndResolvedPokemon(String value) {
        // Split shorthand name and Pokémon nickname
        String[] splitValue = value.split("\\|")[0].split(":"); // ["p1a", "NickName"]
        Trainer trainer = retrieveTrainerForShorthand(splitValue[0].substring(0, 2));
        Pokemon pokemon = pokemonResolverService.resolve(trainer, splitValue[1].trim());
        return new Pair<>(trainer, pokemon);
    }


    /**
     * Retrieves the {@link Trainer} for a given shorthand name
     * @param shorthand the shorthand name to resolve
     * @return the {@link Trainer} with the matching shorthand name
     */
    private Trainer retrieveTrainerForShorthand(String shorthand) {
        return shorthand.contains(results.getTrainerOne().getShortHandName())
                ? results.getTrainerOne()
                : results.getTrainerTwo();
    }

    private void updatePokemon(Trainer trainer, Pokemon pokemonToUpdate){
        List<Pokemon> pokemons = trainerToPokemon.get(trainer.getShortHandName());
        Optional<Pokemon> possiblePokemon = pokemons.stream().filter(pokemon -> pokemon.getUuid().equals(pokemonToUpdate.getUuid()))
                .findFirst();
        possiblePokemon.ifPresent(pokemon -> pokemon.update(pokemonToUpdate));
    }

    private Generation3Action getPreviousActionUntil(String actionTypeLookup) {
        ListIterator<Generation3Action> generation3ActionListIterator = allActions.listIterator(allActions.size());
        Generation3Action previous = null;
        while (generation3ActionListIterator.hasPrevious()) {
            previous = generation3ActionListIterator.previous();
            if(previous.getActionType().equals(actionTypeLookup)) {
                break;
            }
        }

        return previous;
    }

}
