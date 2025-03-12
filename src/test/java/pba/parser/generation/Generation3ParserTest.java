package pba.parser.generation;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;
import pba.models.replay.Replay;
import pba.models.replay.results.Generation3Results;

class Generation3ParserTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void itShouldParser() throws IOException {
    Replay replay = loadInput();
    Generation3Results expected = loadExpected();
    Generation3Parser generation3Parser = new Generation3Parser();
    Generation3Results actual = generation3Parser.parseReplays(List.of(replay));
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  private Replay loadInput() throws IOException {
    URL input =
        Generation3ParserTest.class.getClassLoader().getResource("parser/input/gen3ou.json");
    return mapper.readValue(input, Replay.class);
  }

  private Generation3Results loadExpected() throws IOException {
    URL input =
        Generation3ParserTest.class.getClassLoader().getResource("parser/output/results.json");
    return mapper.readValue(input, Generation3Results.class);
  }
}
