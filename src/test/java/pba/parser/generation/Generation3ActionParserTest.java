package pba.parser.generation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pba.models.Player;
import pba.models.Pokemon;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.replay.generation.Generation3ParsedReplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class Generation3ActionParserTest {

    @ParameterizedTest
    @MethodSource("createGeneration3Action")
    void itShouldParse(Generation3Action action) {

        Map<Player, List<Pokemon>> playerToPokemon = new HashMap<>();
        Generation3ParsedReplay generation3ParsedReplay = new Generation3ParsedReplay(playerToPokemon);
        Generation3ActionParser generation3ActionParser = new Generation3ActionParser(generation3ParsedReplay);

        generation3ActionParser.applyAction(action);
        assertDoesNotThrow(generation3ActionParser::retrieveResults);
    }

    private static Stream<Arguments> createGeneration3Action() {
        return Stream.of(
                Arguments.of(new Generation3Action("|player|p1|PlatyPunkBean|barry|", "p1|PlatyPunkBean|barry|")),
                Arguments.of(new Generation3Action("|switch|", "p1a: Jynx|Jynx, F|100/100")),
                Arguments.of(new Generation3Action("|switch|", "p2a: Breloom|Breloom, M|100/100")),
                Arguments.of(new Generation3Action("|turn|", "1")),
                Arguments.of(new Generation3Action("|upkeep", "")),
                Arguments.of( new Generation3Action("|-weather|", "Sandstorm|[from] ability: Sand Stream|[of] p2a: Tyranitar")),
                Arguments.of(new Generation3Action("|move|", "p1a: Jynx|Ice Beam|p2a: Tyranitar")),
                Arguments.of(new Generation3Action("|-damage|", "p2a: Tyranitar|49/100")),
                Arguments.of( new Generation3Action("|faint|", "p2a: Skarmory"))
        );
    }

}
