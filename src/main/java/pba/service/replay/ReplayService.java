package pba.service.replay;

import pba.models.replay.results.ReplayResult;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface ReplayService<R extends ReplayResult> {

  Mono<R> process(Set<String> replayIds);
}
