package pba.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.Test;
import pba.models.replay.Replay;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ShowdownClientIT {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void itShouldPull() throws IOException {
    ShowdownClient showdownClient = new ShowdownClient();
    showdownClient.setWebClient();
    Mono<Replay> actual = showdownClient.getReplay("gen3ou-2311036422");
    Replay expected = buildExpected();
    StepVerifier.create(actual)
        .assertNext(
            replay -> {
              assertThat(replay).usingRecursiveComparison().isEqualTo(expected);
            })
        .verifyComplete();
  }

  private Replay buildExpected() throws IOException {
    URL expected =
        ShowdownClientIT.class.getClassLoader().getResource("replays/gen-3-ou/replayInput.json");
    return mapper.readValue(expected, Replay.class);
  }
}
