package pba.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pba.client.ShowdownClient;
import pba.models.replay.Replay;
import pba.service.replay.ReplayService;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class ReplayServiceTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Mock private ShowdownClient showdownClient;
  @Mock private ReplayFileReader replayFileReader;

  @InjectMocks private ReplayService replayService;

  @Test
  void itShouldParseAReplay() throws IOException {

    Replay replay = buildExpected();
    File mockFile = mock(File.class);
    when(replayFileReader.read(mockFile)).thenReturn(List.of("gen3ou-2311036422"));
    when(showdownClient.getReplay("gen3ou-2311036422")).thenReturn(Mono.just(replay));

    Assertions.assertDoesNotThrow(() -> replayService.processFile(mockFile));
  }

  private Replay buildExpected() throws IOException {
    URL expected =
        ReplayServiceTest.class
            .getClassLoader()
            .getResource("replays/gen-3-ou/gen3ou-2311036422.json");
    return mapper.readValue(expected, Replay.class);
  }
}
