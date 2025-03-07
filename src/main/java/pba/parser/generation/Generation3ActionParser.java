package pba.parser.generation;

import pba.models.Pair;
import pba.models.Trainer;
import pba.models.Pokemon;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.replay.results.Generation3ReplayResults;
import pba.service.team.PokemonResolverService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Generation3ActionParser implements GenerationActionParser<Generation3Action, Generation3ReplayResults> {

    private final Map<String, List<Pokemon>> trainerToPokemon;
    private final PokemonResolverService pokemonResolverService;
    private final Generation3ReplayResults results = new Generation3ReplayResults();
    // Tracks all actions so far - useful for back tracking
    private final LinkedList<Generation3Action> allActions = new LinkedList<>();

    public Generation3ActionParser(){
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

        // Capture Trainer name + shorthand
        if (currentAction.contains("|player|")) {
            String[] valueSplit = value.split("\\|");

            String shortHandName = valueSplit[0]; // p1
            String trainerName = valueSplit[1]; //  SomeTrainerNickname

            if(results.getTrainerOne() == null){
                results.setTrainerOne(new Trainer(trainerName, shortHandName));
                trainerToPokemon.put(shortHandName, new ArrayList<>());
            } else {
                results.setTrainerTwo(new Trainer(trainerName, shortHandName));
                trainerToPokemon.put(shortHandName, new ArrayList<>());
            }
            return;
        }

        // Capture Pokémon swapping into battlefield and its current health
        // Value contains, handler shortname + Pokémon nickname, Pokémon name + possible gender,
        // and their current health
        // example = p1a: NickName|PokemonName + Gender(?)|100/100
        if (currentAction.contains("|switch|")) {
            String[] valueSplit = value.split("\\|");
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

        /*
         * All pokemon should be resolvable at this point.
         */
        Pair<Trainer, Pokemon> trainerAndPokemon = retrieveTrainerAndResolvedPokemon(value);
        Trainer trainer = trainerAndPokemon.key();
        Pokemon pokemon = trainerAndPokemon.value();

        if (currentAction.contains("|-ability|")) {
            String ability = value.split("\\|")[1];
            pokemon.setAbility(ability);
            updatePokemon(trainer, pokemon);
        }

        if (currentAction.contains("|turn|")) {
            results.incrementTurnCount();
        }

        if (currentAction.contains("|upkeep")) {
            // TODO: SKIP
        }

        if (currentAction.contains("|-weather")) {
            // TODO:
        }

        if (currentAction.contains("|drag|")) {
            // TODO:
        }

        if (currentAction.contains("|move|")) {
            // TODO:
        }

        if (currentAction.contains("|-unboost")) {
            // TODO: low priority
        }

        if (currentAction.contains("|-start")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-resisted")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-singleturn")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-setboost|")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-sideend")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-supereffective")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-sidestart")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-damage|")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-crit|")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-fail|")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-status|")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-miss|")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|-heal|")) {
            // TODO:
            // use allActions
        }

        if (currentAction.contains("|cant|")) {
            // TODO:
        }

        if (currentAction.contains("|faint|")) {
            // TODO:
        }

        if (currentAction.contains("|win|")) {
            // TODO:
        }

    }

    /**
     * Splits a value into a {@link Trainer} and pokemon nick name {@link Pair}
     * @param value the value to split and parse
     * @return a {@link Pair} of {@link Trainer} and {@link Pokemon}
     */
    private Pair<Trainer, String> retrieveTrainerAndPokemonNickName(String value) {
        // Split shorthand name and pokemon nickname
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

    /**
     * Returns the current nodes previous {@link Generation3Action}
     * @return the previous nodes {@link Generation3Action}
     */
    private Generation3Action getPreviousAction() {
        return allActions.listIterator().previous();
    }

}
