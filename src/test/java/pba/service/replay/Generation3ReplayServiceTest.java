package pba.service.replay;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pba.client.ShowdownClient;
import pba.models.replay.Replay;
import pba.models.replay.results.Generation3Results;
import pba.parser.generation.Generation3Parser;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class Generation3ReplayServiceTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Mock private ShowdownClient showdownClient;
  @Mock private Generation3Parser generation3Parser;

  @InjectMocks private Generation3ReplayService replayService;

  @Test
  void itShouldParseAReplay() throws IOException {
    // Setup
    Replay expectedReplay = buildExpectedReplay();
    Generation3Results expected = buildExpectedResults();
    List<String> replayUrls = List.of("https://replay.pokemonshowdown.com/gen3ou-2311036422");

    when(showdownClient.getReplay("https://replay.pokemonshowdown.com/gen3ou-2311036422"))
        .thenReturn(Mono.just(expectedReplay));
    when(generation3Parser.parseReplays(List.of(expectedReplay))).thenReturn(expected);

    // Test
    Mono<Generation3Results> actual = replayService.process(replayUrls);

    // Verify
    StepVerifier.create(actual).expectNext(expected).verifyComplete();
  }

  private Replay buildExpectedReplay() throws IOException {
    URL expected =
        Generation3ReplayService.class
            .getClassLoader()
            .getResource("replays/gen-3-ou/replayInput.json");
    return mapper.readValue(expected, Replay.class);
  }

  private Generation3Results buildExpectedResults() throws IOException {
    URL expected =
        Generation3ReplayService.class
            .getClassLoader()
            .getResource("replays/gen-3-ou/results.json");
    return mapper.readValue(expected, Generation3Results.class);
  }
}
