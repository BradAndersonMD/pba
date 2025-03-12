package pba.parser.generation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pba.models.replay.Replay;
import pba.models.replay.data.Generation3ReplayData;
import pba.models.replay.results.Generation3Results;
import pba.parser.ReplayParser;
import pba.service.analyzer.Generation3ResultsAnalyzer;
import pba.service.writer.Generation3ResultsWriter;

@Slf4j
@Component
@RequiredArgsConstructor
public class Generation3Parser extends ReplayParser {

  private final Generation3ResultsAnalyzer generation3ResultsAnalyzer =
      new Generation3ResultsAnalyzer();
  private final Generation3ResultsWriter resultsWriter;

  @Override
  public void parseReplays(List<Replay> replays) {
    List<Generation3ReplayData> replayData =
        replays.stream()
            .map(
                replay -> {
                  log.info("Parsing replay = [{}]", replay.getId());
                  return replay.getLog();
                })
            .map(replayLog -> replayLog.split("\n"))
            .map(
                lines -> {
                  Generation3ActionParser generation3ActionParser = new Generation3ActionParser();

                  return generation3ActionParser.parseLines(lines);
                })
            .toList();

      Generation3Results results = generation3ResultsAnalyzer.analyze(replayData);
      resultsWriter.write(results);
  }
}
