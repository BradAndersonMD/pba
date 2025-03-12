package pba.service.replay;

import java.io.File;
import java.util.List;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pba.client.ShowdownClient;
import pba.models.replay.Replay;
import pba.parser.generation.Generation3Parser;
import pba.service.reader.ReplayFileReader;
import pba.service.writer.Generation3ResultsWriter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplayService {

    private final ShowdownClient showdownClient;
    private final ReplayFileReader replayFileReader;
    private final Generation3Parser replayParser;
    private final Generation3ResultsWriter resultsWriter;

    public void processFile(File replaysFile) {
        List<String> replayIds = replayFileReader.read(replaysFile);
        List<Mono<Replay>> showdownReplays = replayIds.stream().map(showdownClient::getReplay).toList();
        Flux.fromIterable(showdownReplays)
            .flatMap(Function.identity())
            .collectList()
            .map(replayParser::parseReplays)
            .doOnNext(resultsWriter::write)
            .doOnError(err -> log.error("Failed to parse replays", err))
            .block();

        log.info("Finished processing [{}] replay(s)", showdownReplays.size());
    }
}
