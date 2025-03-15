package pba.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pba.models.replay.results.Generation3Results;
import pba.service.replay.Generation3ReplayService;
import pba.service.writer.CsvWriter;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("generation3")
@RestController
public class Generation3Controller {

  private final Generation3ReplayService replayService;
  private final CsvWriter csvWriter;

  @PostMapping("/analyze")
  public Mono<Generation3Results> analyze(@RequestBody List<String> replayUrls) {
    return replayService.process(replayUrls);
  }

  @PostMapping("/analyzeAndDownload")
  public Mono<Void> analyzeAndDownload(
      ServerHttpResponse response, @RequestBody List<String> replayUrls) {
    Hooks.onOperatorDebug();
    return replayService.process(replayUrls).flatMap(results -> csvWriter.write(response, results));
  }
}
