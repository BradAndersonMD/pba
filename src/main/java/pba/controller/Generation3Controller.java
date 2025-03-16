package pba.controller;

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

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("generation3")
@RestController
public class Generation3Controller {

  private final Generation3ReplayService replayService;
  private final CsvWriter csvWriter;

  /**
   * Fetches and analyzes a set of replay urls
   * @param replayUrls the set of replay urls to analyze
   * @return the analysis/results for a given set of replay urls
   */
  @PostMapping("/analyze")
  public Mono<Generation3Results> analyze(@RequestBody Set<String> replayUrls) {
    return replayService.process(replayUrls);
  }

  /**
   * Fetches and analyzes a set of replay urls. Provides a CSV file as a download
   * @param response the server response to update a CSV file to download
   * @param replayUrls the replay urls to analyze
   * @return the analysis/results for a given set of replay urls
   */
  @PostMapping("/analyzeAndDownload")
  public Mono<Void> analyzeAndDownload(
      ServerHttpResponse response, @RequestBody Set<String> replayUrls) {
    return replayService.process(replayUrls).flatMap(results -> csvWriter.write(response, results));
  }
}
