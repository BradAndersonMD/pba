package pba.parser.generation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pba.models.replay.Replay;
import pba.models.replay.data.Generation3ReplayData;
import pba.models.replay.results.Generation3Results;
import pba.parser.ReplayParser;
import pba.service.analyzer.Generation3ResultsAnalyzer;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Generation3Parser extends ReplayParser<Generation3Results> {

    private final Generation3ResultsAnalyzer generation3ResultsAnalyzer = new Generation3ResultsAnalyzer();

    @Override
    public Generation3Results parseReplays(List<Replay> replays) {

        List<Generation3ReplayData> results = replays.stream()
            .map(replay -> {
                log.info("Parsing replay = [{}]", replay.getId());
                return replay.getLog();
            })
            .map(replayLog -> replayLog.split("\n"))
            .map(lines -> {
                Generation3ActionParser generation3ActionParser = new Generation3ActionParser();
                return generation3ActionParser.parseLines(lines);
            })
            .toList();

        return generation3ResultsAnalyzer.analyze(results);
    }
}
