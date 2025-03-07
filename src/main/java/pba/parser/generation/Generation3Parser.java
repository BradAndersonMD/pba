package pba.parser.generation;

import lombok.extern.slf4j.Slf4j;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.replay.Replay;
import pba.models.replay.results.Generation3ReplayResults;
import pba.parser.ReplayParser;

import java.util.List;

@Slf4j
public class Generation3Parser implements ReplayParser<Generation3ReplayResults> {

    @Override
    public Generation3ReplayResults parse(Replay replay) {
        String replayLog = replay.getLog();

        List<String> lines = List.of(replayLog.split("\n"));
        Generation3ActionParser generation3ActionParser = new Generation3ActionParser();

        for(String line: lines) {
            Generation3Action action = new Generation3Action(line);
            generation3ActionParser.applyAction(action);
        }

        return generation3ActionParser.retrieveResults();
    }

}
