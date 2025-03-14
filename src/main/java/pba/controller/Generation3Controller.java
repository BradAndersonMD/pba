package pba.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pba.models.replay.results.Generation3Results;
import pba.service.replay.Generation3ReplayService;
import pba.service.writer.CsvWriter;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController("generation3")
public class Generation3Controller {

    private final Generation3ReplayService replayService;
    private final CsvWriter csvWriter;

    @PostMapping("/analyze")
    public Mono<Generation3Results> analyze(@RequestBody List<String> replayUrls) {
        return replayService.process(replayUrls);
    }

    @PostMapping("/analyzeAndDownload")
    public Mono<Void> analyze(ServerHttpResponse response, @RequestBody List<String> replayUrls) {
        return replayService.process(replayUrls).flatMap(results -> csvWriter.write(response, results));
    }

}
