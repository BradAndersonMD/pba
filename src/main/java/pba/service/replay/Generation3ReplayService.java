package pba.service.replay;

import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pba.client.ShowdownClient;
import pba.models.replay.Replay;
import pba.models.replay.results.Generation3Results;
import pba.parser.generation.Generation3Parser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class Generation3ReplayService implements ReplayService<Generation3Results> {

  private final ShowdownClient showdownClient;
  private final Generation3Parser replayParser;

  public Mono<Generation3Results> process(List<String> replayUrls) {
    log.info("Processing [{}] replay(s)", replayUrls.size());

    List<Mono<Replay>> showdownReplays =
        replayUrls.stream().map(showdownClient::getReplay).toList();

    return Flux.fromIterable(showdownReplays)
        .flatMap(Function.identity())
        .collectList()
        .map(replayParser::parseReplays)
        .doOnError(err -> log.error("Failed to parse replays", err))
        .subscribeOn(Schedulers.boundedElastic());
  }

}
