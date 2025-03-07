package pba.parser.generation;

import pba.models.Player;
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

    private final Map<String, List<Pokemon>> playerToPokemon;
    private final PokemonResolverService pokemonResolverService;
    private final Generation3ReplayResults results = new Generation3ReplayResults();
    // Tracks all actions so far - useful for back tracking
    private final LinkedList<Generation3Action> allActions = new LinkedList<>();

    public Generation3ActionParser(){
        this.playerToPokemon = new HashMap<>();
        this.pokemonResolverService = new PokemonResolverService(playerToPokemon);
    }

    @Override
    public void applyAction(Generation3Action action) {
        if(action.isInactionable()){
            allActions.add(action);
            return;
        }

        String currentAction = action.getActionType();
        String value = action.getValue();

        // Capture player name + shorthand
        if (currentAction.contains("|player|")) {
            String[] valueSplit = value.split("\\|");

            String shortHandName = valueSplit[0]; // p1
            String playerName = valueSplit[1]; //  SomePlayerNickname

            if(results.getPlayerOne() == null){
                results.setPlayerOne(new Player(playerName, shortHandName));
                playerToPokemon.put(shortHandName, new ArrayList<>());
            } else {
                results.setPlayerTwo(new Player(playerName, shortHandName));
                playerToPokemon.put(shortHandName, new ArrayList<>());
            }
        }

        // Capture pokemon swapping into battlefield and its current health
        // Value contains, handler shortname + pokemon nickname, pokemon name + possible gender,
        // and their current health
        // example = p1a: NickName|PokemonName + Gender(?)|100/100
        if (currentAction.contains("|switch|")) {
            String[] valueSplit = value.split("\\|");

            // Split shorthand name and pokemon nickname
            String shortHandNameAndPokemonNickName = valueSplit[0]; // "p1a: NickName"
            String[] shortHandAndPokemonNickNameSplit = shortHandNameAndPokemonNickName.split(":"); // ["p1a", "NickName"]
            String shortHandName = shortHandAndPokemonNickNameSplit[0].substring(0, 2); // p1a
            Player player = retrievePlayerForShorthand(shortHandName);
            String pokemonNickName = shortHandAndPokemonNickNameSplit[1]; // NickName

            // Some pokemon don't have genders, but if they do its captured here.
            // We need to hand both cases:
            // Jynx, F
            // Zapdos
            String pokemonNameAndPossibleGender = valueSplit[1];
            String pokemonName = pokemonNameAndPossibleGender.contains(",")
                    ? pokemonNameAndPossibleGender.substring(pokemonNameAndPossibleGender.indexOf(","))
                    : pokemonNameAndPossibleGender;

            // Checks to see if it already exists - add it if missing
            Optional<Pokemon> possiblePokemon = pokemonResolverService.resolve(player, pokemonNickName);
            if (possiblePokemon.isEmpty()){
                Pokemon pokemon = new Pokemon();
                pokemon.setName(pokemonName);
                pokemon.setNickname(pokemonNickName);
                playerToPokemon.get(shortHandName).add(pokemon);
            }

        }

        if (currentAction.contains("|-ability|")) {
            // TODO:
        }

        if (currentAction.contains("|-unboost")) {
            // TODO:
        }

        if (currentAction.contains("|turn|")) {
            // TODO:
        }

        if (currentAction.contains("|upkeep")) {
            // TODO:
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

        allActions.add(action);
    }

    @Override
    public Generation3ReplayResults retrieveResults() {
        results.setAllActions(allActions);
        results.setPlayerOnePokemons(playerToPokemon.get("p1"));
        results.setPlayerTwoPokemons(playerToPokemon.get("p2"));
        return results;
    }

    /**
     * Retrieves the {@link Player} for a given shorthand name
     * @param shorthand the shorthand name to resolve
     * @return the {@link Player} with the matching shorthand name
     */
    private Player retrievePlayerForShorthand(String shorthand) {
        return shorthand.contains(results.getPlayerOne().getShortHandName())
                ? results.getPlayerOne()
                : results.getPlayerTwo();
    }

    /**
     * Returns the current nodes previous {@link Generation3Action}
     * @return the previous nodes {@link Generation3Action}
     */
    private Generation3Action getPreviousAction() {
        return allActions.listIterator().previous();
    }

}
