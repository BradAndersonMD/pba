package pba.models.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pba.models.parser.generation.three.Generation3Action;

class Generation3ActionTest {

  @MethodSource("createGeneration3Action")
  @ParameterizedTest
  void itShouldIdentifyGeneration3Action(String line, Generation3Action expected) {
    Generation3Action actual = new Generation3Action(line);
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  private static Stream<Arguments> createGeneration3Action() {
    return Stream.of(
        Arguments.of("|j|☆PlatyPunkBean", new Generation3Action("|j|", "☆PlatyPunkBean")),
        Arguments.of("|gametype|singles", new Generation3Action("|gametype|", "singles")),
        Arguments.of("|t:|1740939672", new Generation3Action("|t:|", "1740939672")),
        Arguments.of(
            "|switch|p1a: Jynx|Jynx, F|100/100",
            new Generation3Action("|switch|", "p1a: Jynx|Jynx, F|100/100")),
        Arguments.of(
            "|switch|p2a: Breloom|Breloom, M|100/100",
            new Generation3Action("|switch|", "p2a: Breloom|Breloom, M|100/100")),
        Arguments.of("|turn|1", new Generation3Action("|turn|", "1")),
        Arguments.of(
            "|-weather|Sandstorm|[from] ability: Sand Stream|[of] p2a: Tyranitar",
            new Generation3Action(
                "|-weather|", "Sandstorm|[from] ability: Sand Stream|[of] p2a: Tyranitar")),
        Arguments.of(
            "|move|p1a: Jynx|Ice Beam|p2a: Tyranitar",
            new Generation3Action("|move|", "p1a: Jynx|Ice Beam|p2a: Tyranitar")),
        Arguments.of(
            "|-damage|p2a: Tyranitar|49/100",
            new Generation3Action("|-damage|", "p2a: Tyranitar|49/100")),
        Arguments.of("|upkeep", new Generation3Action("|upkeep", "")),
        Arguments.of("|faint|p2a: Skarmory", new Generation3Action("|faint|", "p2a: Skarmory")));
  }
}
