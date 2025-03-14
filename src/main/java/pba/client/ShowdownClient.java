package pba.client;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pba.models.replay.Replay;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ShowdownClient {

  private static final String URL = "https://replay.pokemonshowdown.com/";

  private WebClient webClient;

  @PostConstruct
  protected void setWebClient() {
    webClient = WebClient.builder().baseUrl(URL).build();
  }

  public Mono<Replay> getReplay(String replayUrl) {
    log.info("Fetching replay [{}]", replayUrl);
    String replayId = convertUrl(replayUrl);
    return webClient
        .get()
        .uri(replayId + ".json")
        .retrieve()
        .bodyToMono(Replay.class)
        .doOnError(e -> log.error("Failed to create replay [{}]", replayUrl));
  }

  private String convertUrl(String url) {
    String replayId = url.substring(url.lastIndexOf("/") + 1);
    if (replayId.contains("battle-")) {
      return replayId.substring(replayId.indexOf("-") + 1);
    }
    return replayId;
  }
}
