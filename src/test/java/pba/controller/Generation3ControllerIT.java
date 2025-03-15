package pba.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import pba.models.replay.results.Generation3Results;
import pba.service.replay.Generation3ReplayService;
import pba.service.writer.CsvWriter;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(Generation3Controller.class)
@AutoConfigureWebTestClient
class Generation3ControllerIT {

  @Autowired private WebTestClient webClient;

  @MockitoBean private Generation3ReplayService generation3ReplayService;

  @MockitoBean private CsvWriter csvWriter;

  @Test
  void itShouldAnalyzedResults() {
    List<String> replayUrls = new ArrayList<>();
    replayUrls.add("http://some-showdown-url.com/battle-1");
    replayUrls.add("http://some-showdown-url.com/battle-2");
    replayUrls.add("http://some-showdown-url.com/battle-3");
    when(generation3ReplayService.process(replayUrls))
        .thenReturn(Mono.just(new Generation3Results()));

    webClient
        .post()
        .uri("/generation3/analyze")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(replayUrls))
        .exchange()
        .expectStatus()
        .isOk();

    verify(generation3ReplayService).process(replayUrls);
  }

  @Test
  void itShouldAnalyzeAndDownloadResults() {
    List<String> replayUrls = new ArrayList<>();
    replayUrls.add("http://some-showdown-url.com/battle-1");
    replayUrls.add("http://some-showdown-url.com/battle-2");
    replayUrls.add("http://some-showdown-url.com/battle-3");
    Generation3Results data = new Generation3Results();

    when(generation3ReplayService.process(replayUrls)).thenReturn(Mono.just(data));
    when(csvWriter.write(any(ServerHttpResponse.class), eq(data))).thenReturn(Mono.empty().then());

    webClient
        .post()
        .uri("/generation3/analyzeAndDownload")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(replayUrls))
        .exchange()
        .expectStatus()
        .isOk();

    verify(generation3ReplayService).process(replayUrls);
    verify(csvWriter).write(any(ServerHttpResponse.class), eq(data));
  }
}
