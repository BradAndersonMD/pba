package pba.service.replay;

import pba.models.replay.results.ReplayResult;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReplayService<R extends ReplayResult> {

    Mono<R> process(List<String> replayIds);

}
