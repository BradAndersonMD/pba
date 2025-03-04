package pba.parser.generation;

import pba.models.Player;
import pba.models.Pokemon;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.replay.generation.Generation3ParsedReplay;
import pba.models.replay.results.Generation3ReplayResults;
import pba.service.team.PokemonResolverService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Generation3ActionParser implements GenerationActionParser<Generation3Action, Generation3ReplayResults> {

    private final Map<String, List<Pokemon>> playerToPokemon;
    private final PokemonResolverService pokemonResolverService;
    private final Generation3ReplayResults results = new Generation3ReplayResults();
    // Tracks all actions so far - useful for back tracking
    private final LinkedList<Generation3Action> allActions = new LinkedList<>();

    public Generation3ActionParser(Generation3ParsedReplay generation3ParsedReplay){
        this.playerToPokemon = generation3ParsedReplay.getPlayerToPokemon();
        this.pokemonResolverService = new PokemonResolverService(playerToPokemon);
    }

    @Override
    public void applyAction(Generation3Action action) {
        if(action.isInactionable()){
            allActions.add(action);
            return;
        }

        String actionType = action.getActionType();
        String value = action.getValue();

        if (actionType.contains("|player|")) {
            String[] valueSplit = value.split("\\|");
            String shortHandName = valueSplit[0] + "a";
            playerToPokemon.putIfAbsent(shortHandName, new ArrayList<>());
        }

        if (actionType.contains("|switch|")) {
            String[] valueSplit = value.split("\\|");
            String shortHandName = valueSplit[0]; // "p1a: X|Y"

            //TODO: Retrieve pokemon nickname + name and current health 100/100
//            playerToPokemon.compute(shortHandName, (playerName, pokemons) -> {
//                pokemons.add(new Pokemon());
//                return pokemons;
//            });


        }

        if (actionType.contains("|-ability|")) {

        }

        if (actionType.contains("|-unboost")) {

        }

        if (actionType.contains("|turn|")) {

        }

        if (actionType.contains("|upkeep")) {

        }

        if (actionType.contains("|-weather")) {

        }

        if (actionType.contains("|drag|")) {

        }

        if (actionType.contains("|move|")) {

        }

        if (actionType.contains("|-start")) {
            // use allActions
        }

        if (actionType.contains("|-resisted")) {
            // use allActions
        }

        if (actionType.contains("|-singleturn")) {
            // use allActions
        }

        if (actionType.contains("|-setboost|")) {
            // use allActions
        }

        if (actionType.contains("|-sideend")) {
            // use allActions
        }

        if (actionType.contains("|-supereffective")) {
            // use allActions
        }

        if (actionType.contains("|-sidestart")) {
            // use allActions
        }

        if (actionType.contains("|-damage|")) {
            // use allActions
        }

        if (actionType.contains("|-crit|")) {
            // use allActions
        }

        if (actionType.contains("|-fail|")) {
            // use allActions
        }

        if (actionType.contains("|-status|")) {
            // use allActions
        }

        if (actionType.contains("|-miss|")) {
            // use allActions
        }

        if (actionType.contains("|-heal|")) {
            // use allActions
        }

        if (actionType.contains("|cant|")) {

        }

        if (actionType.contains("|faint|")) {

        }

        if (actionType.contains("|win|")) {

        }

        allActions.add(action);
    }

    @Override
    public Generation3ReplayResults retrieveResults() {
        return results;
    }


}
