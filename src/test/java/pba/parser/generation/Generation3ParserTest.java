package pba.parser.generation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pba.models.replay.Replay;
import pba.models.replay.results.Generation3ReplayResults;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class Generation3ParserTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void itShouldParser() throws IOException {
        Replay replay = loadInput();
        Generation3ReplayResults expected = loadExpected();
        Generation3Parser generation3Parser = new Generation3Parser();
        Generation3ReplayResults actual = generation3Parser.parse(replay);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private Replay loadInput() throws IOException {
        URL input = Generation3ParserTest.class.getClassLoader().getResource("parser/input/gen3ou.json");
        return mapper.readValue(input, Replay.class);
    }

    private Generation3ReplayResults loadExpected() throws IOException {
        URL input = Generation3ParserTest.class.getClassLoader().getResource("parser/output/results.txt");
        // TODO: Read in results and parse them
        return mapper.readValue(input, Generation3ReplayResults.class);
    }

}
