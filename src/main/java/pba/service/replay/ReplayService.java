package pba.service.replay;

import java.util.List;
import pba.models.replay.results.ReplayResult;
import reactor.core.publisher.Mono;

public interface ReplayService<R extends ReplayResult> {

  Mono<R> process(List<String> replayIds);
}
