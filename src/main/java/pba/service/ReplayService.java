package pba.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pba.client.ShowdownClient;
import pba.models.Pair;
import pba.models.replay.GenerationalFormat;
import pba.models.replay.Replay;
import pba.models.replay.results.ReplayResults;
import pba.parser.ReplayParser;
import pba.parser.generation.Generation3Parser;
import pba.service.analyzer.Generation3ResultsAnalyzer;
import pba.service.analyzer.ResultsAnalyzer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static pba.models.replay.GenerationalFormat.GEN_3_OU;

@Slf4j
@Service
@AllArgsConstructor
public class ReplayService {

  private ShowdownClient showdownClient;
  private ReplayFileReader replayFileReader;
  private static final Map<GenerationalFormat, Pair<ReplayParser<?>, ResultsAnalyzer<?>>> generationalFormatToReplayParserPair =
      Map.of(GEN_3_OU, new Pair<>(new Generation3Parser(), new Generation3ResultsAnalyzer()));

  public void processFile(File replaysFile) {

    List<String> replayIds = replayFileReader.read(replaysFile);
    List<Mono<Replay>> replays = replayIds.stream()
                .map(replayId -> showdownClient.getReplay(replayId))
                .toList();

    Flux.fromIterable(replays)
        .flatMap(Function.identity())
        .map(replay -> {

            Pair<ReplayParser<?>, ResultsAnalyzer<?>> replayParserResultsAnalyzerPair = generationalFormatToReplayParserPair.get(replay.getFormatid());
            ReplayParser<?> replayParser = replayParserResultsAnalyzerPair.key();
            ResultsAnalyzer<?> resultsAnalyzer = replayParserResultsAnalyzerPair.value();
            return new Pair<>(replayParser.parse(replay), resultsAnalyzer);

        })
        .doOnNext(resultAndAnalyzerPair -> {

            //we lost typing here...
            ReplayResults results = resultAndAnalyzerPair.key();
            ResultsAnalyzer<?> analyzer = resultAndAnalyzerPair.value();

        })
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe();

    log.info("Finished processing [{}] replay(s)", replays.size());
  }

}
