package pba.parser.generation;

import lombok.extern.slf4j.Slf4j;
import pba.models.Player;
import pba.models.Pokemon;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.replay.Replay;
import pba.models.replay.generation.Generation3ParsedReplay;
import pba.models.replay.results.Generation3ReplayResults;
import pba.parser.ReplayParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Generation3Parser implements ReplayParser<Generation3ReplayResults> {

    @Override
    public Generation3ReplayResults parse(Replay replay) {
        String replayLog = replay.getLog();

        List<String> lines = List.of(replayLog.split("\n"));
        Map<Player, List<Pokemon>> playerToPokemon = new HashMap<>();
        Generation3ParsedReplay generation3ParsedReplay = new Generation3ParsedReplay(playerToPokemon);
        Generation3ActionParser generation3ActionParser = new Generation3ActionParser(generation3ParsedReplay);

        for(String line: lines) {
            Generation3Action action = new Generation3Action(line);
            generation3ActionParser.applyAction(action);
        }

        return generation3ActionParser.retrieveResults();
    }

}
