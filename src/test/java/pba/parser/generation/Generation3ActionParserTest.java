package pba.parser.generation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pba.models.Player;
import pba.models.Pokemon;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.replay.generation.Generation3ParsedReplay;
import pba.models.replay.results.Generation3ReplayResults;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class Generation3ActionParserTest {

    Generation3ActionParser generation3ActionParser = new Generation3ActionParser();

    @ParameterizedTest
    @MethodSource("createGeneration3Action")
    void itShouldParse(Generation3Action action) {
        generation3ActionParser.applyAction(action);
        assertDoesNotThrow(generation3ActionParser::retrieveResults);
    }

    @Test
    void itShouldParse_Player() {
        // Setup
        Generation3Action playerOneAction = new Generation3Action("|player|p1|PBAPoliwraths|baker|", "p1|PBAPoliwraths|baker|");
        Generation3Action playerTwoAction = new Generation3Action("|player|p2|PlatyPunkBean|barry|", "p2|PlatyPunkBean|barry|");

        // Test
        generation3ActionParser.applyAction(playerOneAction);
        generation3ActionParser.applyAction(playerTwoAction);

        // Verify
        Generation3ReplayResults generation3ReplayResults = generation3ActionParser.retrieveResults();

        LinkedList<Generation3Action> allActions = generation3ReplayResults.getAllActions();
        Player playerOne = generation3ReplayResults.getPlayerOne();
        Player playerTwo = generation3ReplayResults.getPlayerTwo();

        assertThat(playerOne.getName()).isEqualTo("PBAPoliwraths");
        assertThat(playerTwo.getName()).isEqualTo("PlatyPunkBean");
        assertThat(allActions).hasSize(2);
    }

    @Test
    void itShouldParse_switch() {
        // Setup
        Generation3Action playerOneAction = new Generation3Action("|player|p1|PBAPoliwraths|baker|", "p1|PBAPoliwraths|baker|");
        Generation3Action playerTwoAction = new Generation3Action("|player|p2|PlatyPunkBean|barry|", "p2|PlatyPunkBean|barry|");
        Generation3Action switchOneAction = new Generation3Action("|switch|", "p1a: Jynx|Jynx, F|100/100");
        Generation3Action switchTwoAction = new Generation3Action("|switch|", "p2a: Zapados|Zapados|100/100");

        // Test
        generation3ActionParser.applyAction(playerOneAction);
        generation3ActionParser.applyAction(playerTwoAction);
        generation3ActionParser.applyAction(switchOneAction);
        generation3ActionParser.applyAction(switchTwoAction);

        // Verify
        Generation3ReplayResults generation3ReplayResults = generation3ActionParser.retrieveResults();

        LinkedList<Generation3Action> allActions = generation3ReplayResults.getAllActions();
        Player playerOne = generation3ReplayResults.getPlayerOne();
        Player playerTwo = generation3ReplayResults.getPlayerTwo();
        List<Pokemon> playerOnePokemons = generation3ReplayResults.getPlayerOnePokemons();
        List<Pokemon> playerTwoPokemons = generation3ReplayResults.getPlayerTwoPokemons();

        assertThat(playerOne.getName()).isEqualTo("PBAPoliwraths");
        assertThat(playerTwo.getName()).isEqualTo("PlatyPunkBean");
        assertThat(allActions).hasSize(4);
        assertThat(playerOnePokemons).hasSize(1);
        assertThat(playerTwoPokemons).hasSize(1);

    }


    private static Stream<Arguments> createGeneration3Action() {
        return Stream.of(
                Arguments.of(new Generation3Action("|player|p1|PlatyPunkBean|barry|", "p1|PlatyPunkBean|barry|")),
                Arguments.of(new Generation3Action("|switch|", "p1a: Jynx|Jynx, F|100/100")),
                Arguments.of(new Generation3Action("|switch|", "p1a: Brad|Zapdos|100/100")),
                Arguments.of(new Generation3Action("|turn|", "1")),
                Arguments.of(new Generation3Action("|upkeep", "")),
                Arguments.of( new Generation3Action("|-weather|", "Sandstorm|[from] ability: Sand Stream|[of] p2a: Tyranitar")),
                Arguments.of(new Generation3Action("|move|", "p1a: Jynx|Ice Beam|p2a: Tyranitar")),
                Arguments.of(new Generation3Action("|-damage|", "p2a: Tyranitar|49/100")),
                Arguments.of( new Generation3Action("|faint|", "p2a: Skarmory"))
        );
    }

}
