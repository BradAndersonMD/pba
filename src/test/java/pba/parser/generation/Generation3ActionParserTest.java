package pba.parser.generation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import pba.models.Pair;
import pba.models.parser.generation.three.Generation3Action;
import pba.models.parser.generation.three.StatusEffect;
import pba.models.pokemon.Pokemon;
import pba.models.pokemon.Trainer;
import pba.models.replay.data.Generation3ReplayData;

class Generation3ActionParserTest {

  @Test
  void itShouldParse_Player() throws IOException {
    Generation3ActionParser generation3ActionParser = new Generation3ActionParser();
    // Setup
    List<String> lines = readInputCreateApplyActions("playerLog.log");

    // Test
    Generation3ReplayData generation3ReplayData =
        generation3ActionParser.parseLines(lines.toArray(new String[0]));

    // Verify
    LinkedList<Generation3Action> allActions = generation3ReplayData.getAllActions();
    Trainer trainerOne = generation3ReplayData.getTrainerOne();
    Trainer trainerTwo = generation3ReplayData.getTrainerTwo();

    assertThat(trainerOne.getName()).isEqualTo("PBAPoliwraths");
    assertThat(trainerTwo.getName()).isEqualTo("PlatyPunkBean");
    assertThat(allActions).hasSize(2);
  }

  @Test
  void itShouldParse_switch() throws IOException {
    Generation3ActionParser generation3ActionParser = new Generation3ActionParser();

    // Setup
    List<String> lines = readInputCreateApplyActions("switchLog.log");

    // Test
    Generation3ReplayData generation3ReplayData =
        generation3ActionParser.parseLines(lines.toArray(new String[0]));

    // Verify
    LinkedList<Generation3Action> allActions = generation3ReplayData.getAllActions();
    Trainer trainerOne = generation3ReplayData.getTrainerOne();
    Trainer trainerTwo = generation3ReplayData.getTrainerTwo();
    Pokemon actualPlayerOnePokemon = generation3ReplayData.getTrainerOnePokemons().get(0);
    Pokemon actualPlayerTwoPokemon = generation3ReplayData.getTrainerTwoPokemons().get(0);

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
    assertThat(actualPlayerOnePokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerOnePokemon);
    assertThat(actualPlayerTwoPokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerTwoPokemon);
  }

  @Test
  void itShouldParse_Damage() throws IOException {
    Generation3ActionParser generation3ActionParser = new Generation3ActionParser();

    // Setup
    List<String> lines = readInputCreateApplyActions("damageLog.log");

    // Test
    Generation3ReplayData generation3ReplayData =
        generation3ActionParser.parseLines(lines.toArray(new String[0]));

    LinkedList<Generation3Action> allActions = generation3ReplayData.getAllActions();
    Trainer trainerOne = generation3ReplayData.getTrainerOne();
    Trainer trainerTwo = generation3ReplayData.getTrainerTwo();
    Pokemon actualPlayerOnePokemon = generation3ReplayData.getTrainerOnePokemons().get(0);
    Pokemon actualPlayerTwoPokemon = generation3ReplayData.getTrainerTwoPokemons().get(0);

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
    expectedPlayerTwoPokemon.setDamageTaken(36);

    assertThat(trainerOne.getName()).isEqualTo("PlatyPunkBean");
    assertThat(trainerTwo.getName()).isEqualTo("PBAPoliwraths");
    assertThat(allActions).hasSize(6);
    assertThat(actualPlayerOnePokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerOnePokemon);
    assertThat(actualPlayerTwoPokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerTwoPokemon);
  }

  @Test
  void itShouldParse_Heal() throws IOException {
    Generation3ActionParser generation3ActionParser = new Generation3ActionParser();

    // Setup
    List<String> lines = readInputCreateApplyActions("healLog.log");

    // Test
    Generation3ReplayData generation3ReplayData =
        generation3ActionParser.parseLines(lines.toArray(new String[0]));

    LinkedList<Generation3Action> allActions = generation3ReplayData.getAllActions();
    Trainer trainerOne = generation3ReplayData.getTrainerOne();
    Trainer trainerTwo = generation3ReplayData.getTrainerTwo();
    Pokemon actualPlayerOnePokemon = generation3ReplayData.getTrainerOnePokemons().get(0);
    Pokemon actualPlayerTwoPokemon = generation3ReplayData.getTrainerTwoPokemons().get(0);

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
    expectedPlayerTwoPokemon.setDamageTaken(36);

    assertThat(trainerOne.getName()).isEqualTo("PlatyPunkBean");
    assertThat(trainerTwo.getName()).isEqualTo("PBAPoliwraths");
    assertThat(allActions).hasSize(7);
    assertThat(actualPlayerOnePokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerOnePokemon);
    assertThat(actualPlayerTwoPokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerTwoPokemon);
  }

  @Test
  void itShouldParse_Weather() throws IOException {
    Generation3ActionParser generation3ActionParser = new Generation3ActionParser();

    // Setup
    List<String> lines = readInputCreateApplyActions("weatherLog.log");

    // Test
    Generation3ReplayData generation3ReplayData =
        generation3ActionParser.parseLines(lines.toArray(new String[0]));

    // Verify
    LinkedList<Generation3Action> allActions = generation3ReplayData.getAllActions();
    Trainer trainerOne = generation3ReplayData.getTrainerOne();
    Trainer trainerTwo = generation3ReplayData.getTrainerTwo();
    Pokemon actualPlayerOnePokemon = generation3ReplayData.getTrainerOnePokemons().get(0);
    Pokemon actualPlayerTwoPokemon = generation3ReplayData.getTrainerTwoPokemons().get(0);

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
    expectedPlayerTwoPokemon.setDamageTaken(6);

    assertThat(trainerOne.getName()).isEqualTo("PlatyPunkBean");
    assertThat(trainerTwo.getName()).isEqualTo("PBAPoliwraths");
    assertThat(allActions).hasSize(6);
    assertThat(actualPlayerOnePokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerOnePokemon);
    assertThat(actualPlayerTwoPokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerTwoPokemon);
  }

  @Test
  void itShouldParse_StatusEffect() throws IOException {
    Generation3ActionParser generation3ActionParser = new Generation3ActionParser();

    // Setup
    List<String> lines = readInputCreateApplyActions("statusEffectLog.log");

    // Test
    Generation3ReplayData generation3ReplayData =
        generation3ActionParser.parseLines(lines.toArray(new String[0]));

    LinkedList<Generation3Action> allActions = generation3ReplayData.getAllActions();
    Trainer trainerOne = generation3ReplayData.getTrainerOne();
    Trainer trainerTwo = generation3ReplayData.getTrainerTwo();
    Pokemon actualPlayerOnePokemon = generation3ReplayData.getTrainerOnePokemons().get(0);
    Pokemon actualPlayerTwoPokemon = generation3ReplayData.getTrainerTwoPokemons().get(0);

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
    expectedPlayerTwoPokemon.setDamageTaken(12);
    expectedPlayerTwoPokemon.setStatusEffectAndPokemon(
        new Pair<>(StatusEffect.BURN, generation3ReplayData.getTrainerOnePokemons().get(0)));

    assertThat(trainerOne.getName()).isEqualTo("PlatyPunkBean");
    assertThat(trainerTwo.getName()).isEqualTo("PBAPoliwraths");
    assertThat(allActions).hasSize(7);
    assertThat(actualPlayerOnePokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerOnePokemon);
    assertThat(actualPlayerTwoPokemon)
        .usingRecursiveComparison()
        .ignoringFields("uuid")
        .isEqualTo(expectedPlayerTwoPokemon);
  }

  private List<String> readInputCreateApplyActions(String fileName) throws IOException {
    URL resource =
        Generation3ActionParserTest.class.getClassLoader().getResource("parser/input/" + fileName);
    File file = new File(resource.getFile());

    List<String> list;
    try (FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      list = bufferedReader.lines().toList();
    }

    return list;
  }
}
