package pba.parser.generation;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import pba.models.replay.Replay;
import pba.models.replay.results.Generation3Results;

class Generation3ParserTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void itShouldParser_SingleReplay() throws IOException {
    Replay replay = loadInput();
    Generation3Results expected = loadExpected("results.json");
    Generation3Parser generation3Parser = new Generation3Parser();
    Generation3Results actual = generation3Parser.parseReplays(List.of(replay));
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void itShouldParser_MultipleReplays() throws IOException {
    List<Replay> replays = loadInputs();
    Generation3Results expected = loadExpected("multipleReplayResults.json");
    Generation3Parser generation3Parser = new Generation3Parser();
    Generation3Results actual = generation3Parser.parseReplays(replays);
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  private Replay loadInput() throws IOException {
    URL input =
        Generation3ParserTest.class.getClassLoader().getResource("parser/input/gen3ou.json");
    return mapper.readValue(input, Replay.class);
  }

  private List<Replay> loadInputs() throws IOException {
    List<Replay> replays = new ArrayList<>();
    List<String> replayFilenames =
        List.of(
            "gen3ou-2311036422.json",
            "gen3ou-2312634390.json",
            "gen3ou-2312642550.json",
            "gen3ou-2312646506.json");

    for (String fileName : replayFilenames) {
      URL resource =
          Generation3ParserTest.class.getClassLoader().getResource("parser/input/" + fileName);
      Replay replay = mapper.readValue(resource, Replay.class);
      replays.add(replay);
    }

    return replays;
  }

  private Generation3Results loadExpected(String resultsFileName) throws IOException {
    URL input =
        Generation3ParserTest.class
            .getClassLoader()
            .getResource("parser/output/" + resultsFileName);
    return mapper.readValue(input, Generation3Results.class);
  }
}
