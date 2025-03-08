package pba.parser.generation;

import org.junit.jupiter.api.Test;
import pba.models.Pair;
import pba.models.StatusEffect;
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
        assertThat(actualPlayerOnePokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerOnePokemon);
        assertThat(actualPlayerTwoPokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerTwoPokemon);
    }

    @Test
    void itShouldParse_Damage() {
        // Setup
        Generation3Action playerOneAction = new Generation3Action("|player|p1|PlatyPunkBean|baker|", "p1|PlatyPunkBean|baker|");
        Generation3Action playerTwoAction = new Generation3Action("|player|p2|PBAPoliwraths|barry|", "p2|PBAPoliwraths|barry|");
        Generation3Action switchOneAction = new Generation3Action("|switch|", "p1a: Gengar|Gengar, M|100/100");
        Generation3Action switchTwoAction = new Generation3Action("|switch|", "p2a: Swampert|Swampert, F|100/100");
        Generation3Action moveAction = new Generation3Action("|move|", "p1a: Gengar|Ice Punch|p2a: Swampert");
        Generation3Action damageAction = new Generation3Action("|-damage|", "p2a: Swampert|64/100 brn");

        // Test
        playGeneration3Actions(playerOneAction, playerTwoAction, switchOneAction, switchTwoAction, moveAction, damageAction);

        // Verify
        Generation3ReplayResults generation3ReplayResults = generation3ActionParser.retrieveResults();

        LinkedList<Generation3Action> allActions = generation3ReplayResults.getAllActions();
        Trainer trainerOne = generation3ReplayResults.getTrainerOne();
        Trainer trainerTwo = generation3ReplayResults.getTrainerTwo();
        Pokemon actualPlayerOnePokemon = generation3ReplayResults.getTrainerOnePokemons().get(0);
        Pokemon actualPlayerTwoPokemon = generation3ReplayResults.getTrainerTwoPokemons().get(0);

        Pokemon expectedPlayerOnePokemon = new Pokemon();
        expectedPlayerOnePokemon.setName("Gengar");
        expectedPlayerOnePokemon.setNickname("Gengar");
        expectedPlayerOnePokemon.setTrainer(trainerOne);
        expectedPlayerOnePokemon.setCurrentHealth(100);
        expectedPlayerOnePokemon.setDamageDealt(36);
        Pokemon expectedPlayerTwoPokemon = new Pokemon();
        expectedPlayerTwoPokemon.setName("Swampert");
        expectedPlayerTwoPokemon.setNickname("Swampert");
        expectedPlayerTwoPokemon.setTrainer(trainerTwo);
        expectedPlayerTwoPokemon.setCurrentHealth(64);

        assertThat(trainerOne.getName()).isEqualTo("PlatyPunkBean");
        assertThat(trainerTwo.getName()).isEqualTo("PBAPoliwraths");
        assertThat(allActions).hasSize(6);
        assertThat(actualPlayerOnePokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerOnePokemon);
        assertThat(actualPlayerTwoPokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerTwoPokemon);
    }

    @Test
    void itShouldParse_Heal() {
        // Setup
        Generation3Action playerOneAction = new Generation3Action("|player|p1|PlatyPunkBean|baker|", "p1|PlatyPunkBean|baker|");
        Generation3Action playerTwoAction = new Generation3Action("|player|p2|PBAPoliwraths|barry|", "p2|PBAPoliwraths|barry|");
        Generation3Action switchOneAction = new Generation3Action("|switch|", "p1a: Gengar|Gengar, M|100/100");
        Generation3Action switchTwoAction = new Generation3Action("|switch|", "p2a: Swampert|Swampert, F|100/100");
        Generation3Action moveAction = new Generation3Action("|move|", "p1a: Gengar|Ice Punch|p2a: Swampert");
        Generation3Action damageAction = new Generation3Action("|-damage|", "p2a: Swampert|64/100 brn");
        Generation3Action healAction = new Generation3Action("|-heal|", "p2a: Swampert|71/100|[from] item: Leftovers");

        // Test
        playGeneration3Actions(playerOneAction, playerTwoAction, switchOneAction, switchTwoAction, moveAction, damageAction, healAction);

        // Verify
        Generation3ReplayResults generation3ReplayResults = generation3ActionParser.retrieveResults();

        LinkedList<Generation3Action> allActions = generation3ReplayResults.getAllActions();
        Trainer trainerOne = generation3ReplayResults.getTrainerOne();
        Trainer trainerTwo = generation3ReplayResults.getTrainerTwo();
        Pokemon actualPlayerOnePokemon = generation3ReplayResults.getTrainerOnePokemons().get(0);
        Pokemon actualPlayerTwoPokemon = generation3ReplayResults.getTrainerTwoPokemons().get(0);

        Pokemon expectedPlayerOnePokemon = new Pokemon();
        expectedPlayerOnePokemon.setName("Gengar");
        expectedPlayerOnePokemon.setNickname("Gengar");
        expectedPlayerOnePokemon.setTrainer(trainerOne);
        expectedPlayerOnePokemon.setDamageDealt(36);
        Pokemon expectedPlayerTwoPokemon = new Pokemon();
        expectedPlayerTwoPokemon.setName("Swampert");
        expectedPlayerTwoPokemon.setNickname("Swampert");
        expectedPlayerTwoPokemon.setTrainer(trainerTwo);
        expectedPlayerTwoPokemon.setCurrentHealth(71);

        assertThat(trainerOne.getName()).isEqualTo("PlatyPunkBean");
        assertThat(trainerTwo.getName()).isEqualTo("PBAPoliwraths");
        assertThat(allActions).hasSize(7);
        assertThat(actualPlayerOnePokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerOnePokemon);
        assertThat(actualPlayerTwoPokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerTwoPokemon);
    }

    @Test
    void itShouldParse_Weather() {
        // Setup
        Generation3Action playerOneAction = new Generation3Action("|player|p1|PlatyPunkBean|baker|", "p1|PlatyPunkBean|baker|");
        Generation3Action playerTwoAction = new Generation3Action("|player|p2|PBAPoliwraths|barry|", "p2|PBAPoliwraths|barry|");
        Generation3Action switchOneAction = new Generation3Action("|switch|", "p1a: Gengar|Gengar, M|100/100");
        Generation3Action switchTwoAction = new Generation3Action("|switch|", "p2a: Swampert|Swampert, F|100/100");
        Generation3Action weatherAction = new Generation3Action("|-weather|", "Sandstorm|[from] ability: Sand Stream|[of] p1a: Gengar");
        Generation3Action damageAction = new Generation3Action("|-damage|", "p2a: Swampert|94/100|[from] Sandstorm");

        // Test
        playGeneration3Actions(playerOneAction, playerTwoAction, switchOneAction, switchTwoAction, weatherAction, damageAction);

        // Verify
        Generation3ReplayResults generation3ReplayResults = generation3ActionParser.retrieveResults();

        LinkedList<Generation3Action> allActions = generation3ReplayResults.getAllActions();
        Trainer trainerOne = generation3ReplayResults.getTrainerOne();
        Trainer trainerTwo = generation3ReplayResults.getTrainerTwo();
        Pokemon actualPlayerOnePokemon = generation3ReplayResults.getTrainerOnePokemons().get(0);
        Pokemon actualPlayerTwoPokemon = generation3ReplayResults.getTrainerTwoPokemons().get(0);

        Pokemon expectedPlayerOnePokemon = new Pokemon();
        expectedPlayerOnePokemon.setName("Gengar");
        expectedPlayerOnePokemon.setNickname("Gengar");
        expectedPlayerOnePokemon.setTrainer(trainerOne);
        expectedPlayerOnePokemon.setCurrentHealth(100);
        expectedPlayerOnePokemon.setDamageDealt(6);
        expectedPlayerOnePokemon.setAbility("Sand Stream");
        Pokemon expectedPlayerTwoPokemon = new Pokemon();
        expectedPlayerTwoPokemon.setName("Swampert");
        expectedPlayerTwoPokemon.setNickname("Swampert");
        expectedPlayerTwoPokemon.setTrainer(trainerTwo);
        expectedPlayerTwoPokemon.setCurrentHealth(94);

        assertThat(trainerOne.getName()).isEqualTo("PlatyPunkBean");
        assertThat(trainerTwo.getName()).isEqualTo("PBAPoliwraths");
        assertThat(allActions).hasSize(6);
        assertThat(actualPlayerOnePokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerOnePokemon);
        assertThat(actualPlayerTwoPokemon).usingRecursiveComparison().ignoringFields("uuid").isEqualTo(expectedPlayerTwoPokemon);
    }

    @Test
    void itShouldParse_StatusEffect() {
        // Setup
        Generation3Action playerOneAction = new Generation3Action("|player|p1|PlatyPunkBean|baker|", "p1|PlatyPunkBean|baker|");
        Generation3Action playerTwoAction = new Generation3Action("|player|p2|PBAPoliwraths|barry|", "p2|PBAPoliwraths|barry|");
        Generation3Action switchOneAction = new Generation3Action("|switch|", "p1a: Gengar|Gengar, M|100/100");
        Generation3Action switchTwoAction = new Generation3Action("|switch|", "p2a: Swampert|Swampert, F|100/100");
        Generation3Action moveAction = new Generation3Action("|move|", "p1a: Gengar|Will-O-Wisp|p2a: Swampert");
        Generation3Action statusAction = new Generation3Action("|-status|", "p2a: Swampert|brn");
        Generation3Action damageAction = new Generation3Action("|-damage|", "p2a: Swampert|88/100|[from] brn");

        // Test
        playGeneration3Actions(playerOneAction, playerTwoAction, switchOneAction, switchTwoAction, moveAction, statusAction, damageAction);

        // Verify
        Generation3ReplayResults generation3ReplayResults = generation3ActionParser.retrieveResults();

        LinkedList<Generation3Action> allActions = generation3ReplayResults.getAllActions();
        Trainer trainerOne = generation3ReplayResults.getTrainerOne();
        Trainer trainerTwo = generation3ReplayResults.getTrainerTwo();
        Pokemon actualPlayerOnePokemon = generation3ReplayResults.getTrainerOnePokemons().get(0);
        Pokemon actualPlayerTwoPokemon = generation3ReplayResults.getTrainerTwoPokemons().get(0);

        Pokemon expectedPlayerOnePokemon = new Pokemon();
        expectedPlayerOnePokemon.setName("Gengar");
        expectedPlayerOnePokemon.setNickname("Gengar");
        expectedPlayerOnePokemon.setTrainer(trainerOne);
        expectedPlayerOnePokemon.setCurrentHealth(100);
        expectedPlayerOnePokemon.setDamageDealt(12);
        Pokemon expectedPlayerTwoPokemon = new Pokemon();
        expectedPlayerTwoPokemon.setName("Swampert");
        expectedPlayerTwoPokemon.setNickname("Swampert");
        expectedPlayerTwoPokemon.setTrainer(trainerTwo);
        expectedPlayerTwoPokemon.setCurrentHealth(88);
        expectedPlayerTwoPokemon.setStatusEffectAndPokemon(new Pair<>(StatusEffect.BURN, generation3ReplayResults.getTrainerOnePokemons().get(0)));

        assertThat(trainerOne.getName()).isEqualTo("PlatyPunkBean");
        assertThat(trainerTwo.getName()).isEqualTo("PBAPoliwraths");
        assertThat(allActions).hasSize(7);
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
