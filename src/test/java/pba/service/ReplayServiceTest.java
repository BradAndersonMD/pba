package pba.service;

import static org.mockito.Mockito.doNothing;
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
import pba.models.replay.results.Generation3Results;
import pba.parser.generation.Generation3Parser;
import pba.service.replay.ReplayService;
import pba.service.writer.Generation3ResultsWriter;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class ReplayServiceTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Mock private ShowdownClient showdownClient;
  @Mock private ReplayFileReader replayFileReader;
  @Mock private Generation3Parser generation3Parser;
  @Mock private Generation3ResultsWriter resultsWriter;

  @InjectMocks private ReplayService replayService;

  @Test
  void itShouldParseAReplay() throws IOException {

    Replay replay = buildExpected();
    File mockFile = mock(File.class);
    Generation3Results mockGeneration3Results = mock(Generation3Results.class);
    when(replayFileReader.read(mockFile)).thenReturn(List.of("gen3ou-2311036422"));
    when(showdownClient.getReplay("gen3ou-2311036422")).thenReturn(Mono.just(replay));
    when(generation3Parser.parseReplays(List.of(replay))).thenReturn(mockGeneration3Results);

    doNothing().when(resultsWriter).write(mockGeneration3Results);

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
