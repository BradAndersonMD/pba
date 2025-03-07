package pba.parser.generation;

import org.junit.jupiter.api.Test;
import pba.models.Trainer;
import pba.models.Pokemon;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.replay.results.Generation3ReplayResults;

import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

class Generation3ActionParserTest {

    Generation3ActionParser generation3ActionParser = new Generation3ActionParser();

     @Test
    void itShouldParse_Player() {
        // Setup
        Generation3Action playerOneAction = new Generation3Action("|player|p1|PBAPoliwraths|baker|", "p1|PBAPoliwraths|baker|");
        Generation3Action playerTwoAction = new Generation3Action("|player|p2|PlatyPunkBean|barry|", "p2|PlatyPunkBean|barry|");

        // Test
        playGeneration3Actions(playerOneAction, playerTwoAction);

        // Verify
        Generation3ReplayResults generation3ReplayResults = generation3ActionParser.retrieveResults();

        LinkedList<Generation3Action> allActions = generation3ReplayResults.getAllActions();
        Trainer trainerOne = generation3ReplayResults.getTrainerOne();
        Trainer trainerTwo = generation3ReplayResults.getTrainerTwo();

        assertThat(trainerOne.getName()).isEqualTo("PBAPoliwraths");
        assertThat(trainerTwo.getName()).isEqualTo("PlatyPunkBean");
        assertThat(allActions).hasSize(2);
    }

    @Test
    void itShouldParse_switch() {
        // Setup
        Generation3Action playerOneAction = new Generation3Action("|player|p1|PBAPoliwraths|baker|", "p1|PBAPoliwraths|baker|");
        Generation3Action playerTwoAction = new Generation3Action("|player|p2|PlatyPunkBean|barry|", "p2|PlatyPunkBean|barry|");
        Generation3Action switchOneAction = new Generation3Action("|switch|", "p1a: JynxNick|Jynx, F|100/100");
        Generation3Action switchTwoAction = new Generation3Action("|switch|", "p2a: ZapadosNick|Zapados|100/100");

        // Test
        playGeneration3Actions(playerOneAction, playerTwoAction, switchOneAction, switchTwoAction);

        // Verify
        Generation3ReplayResults generation3ReplayResults = generation3ActionParser.retrieveResults();

        LinkedList<Generation3Action> allActions = generation3ReplayResults.getAllActions();
        Trainer trainerOne = generation3ReplayResults.getTrainerOne();
        Trainer trainerTwo = generation3ReplayResults.getTrainerTwo();
        Pokemon actualPlayerOnePokemon = generation3ReplayResults.getTrainerOnePokemons().get(0);
        Pokemon actualPlayerTwoPokemon = generation3ReplayResults.getTrainerTwoPokemons().get(0);

        Pokemon expectedPlayerOnePokemon = new Pokemon();
        expectedPlayerOnePokemon.setName("Jynx");
        expectedPlayerOnePokemon.setNickname("JynxNick");
        expectedPlayerOnePokemon.setTrainer(trainerOne);
        Pokemon expectedPlayerTwoPokemon = new Pokemon();
        expectedPlayerTwoPokemon.setName("Zapados");
        expectedPlayerTwoPokemon.setNickname("ZapadosNick");
        expectedPlayerTwoPokemon.setTrainer(trainerTwo);

        assertThat(trainerOne.getName()).isEqualTo("PBAPoliwraths");
        assertThat(trainerTwo.getName()).isEqualTo("PlatyPunkBean");
        assertThat(allActions).hasSize(4);
        assertThat(actualPlayerOnePokemon).usingRecursiveComparison().isEqualTo(expectedPlayerOnePokemon);
        assertThat(actualPlayerTwoPokemon).usingRecursiveComparison().isEqualTo(expectedPlayerTwoPokemon);
    }

    @Test
    void itShouldParse_ability() {
        // Setup
        Generation3Action playerOneAction = new Generation3Action("|player|p1|PBAPoliwraths|baker|", "p1|PBAPoliwraths|baker|");
        Generation3Action playerTwoAction = new Generation3Action("|player|p2|PlatyPunkBean|barry|", "p2|PlatyPunkBean|barry|");
        Generation3Action switchOneAction = new Generation3Action("|switch|", "p1a: Gardevoir|Gardevoir, F|100/100");
        Generation3Action switchTwoAction = new Generation3Action("|switch|", "p2a: Jirachi|Jirachi|100/100");
        Generation3Action abilityAction = new Generation3Action("|-ability|", "p1a: Gardevoir|Serene Grace|[from] ability: Trace|[of] p2a: Jirachi");

        // Test
        playGeneration3Actions(playerOneAction, playerTwoAction, switchOneAction, switchTwoAction, abilityAction);

        // Verify
        Generation3ReplayResults generation3ReplayResults = generation3ActionParser.retrieveResults();

        LinkedList<Generation3Action> allActions = generation3ReplayResults.getAllActions();
        Trainer trainerOne = generation3ReplayResults.getTrainerOne();
        Trainer trainerTwo = generation3ReplayResults.getTrainerTwo();
        Pokemon actualPlayerOnePokemon = generation3ReplayResults.getTrainerOnePokemons().get(0);
        Pokemon actualPlayerTwoPokemon = generation3ReplayResults.getTrainerTwoPokemons().get(0);

        Pokemon expectedPlayerOnePokemon = new Pokemon();
        expectedPlayerOnePokemon.setName("Gardevoir");
        expectedPlayerOnePokemon.setNickname("Gardevoir");
        expectedPlayerOnePokemon.setTrainer(trainerOne);
        expectedPlayerOnePokemon.setAbility("Serene Grace");
        Pokemon expectedPlayerTwoPokemon = new Pokemon();
        expectedPlayerTwoPokemon.setName("Jirachi");
        expectedPlayerTwoPokemon.setNickname("Jirachi");
        expectedPlayerTwoPokemon.setTrainer(trainerTwo);

        assertThat(trainerOne.getName()).isEqualTo("PBAPoliwraths");
        assertThat(trainerTwo.getName()).isEqualTo("PlatyPunkBean");
        assertThat(allActions).hasSize(5);
        assertThat(actualPlayerOnePokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerOnePokemon);
        assertThat(actualPlayerTwoPokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerTwoPokemon);
    }

    /**
     * Action order matters here
     * @param actions the actions to apply
     */
    private void playGeneration3Actions(Generation3Action ... actions){
        for (Generation3Action action: actions){
            generation3ActionParser.applyAction(action);
        }
    }

}
