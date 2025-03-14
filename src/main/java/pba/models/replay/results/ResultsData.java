package pba.models.replay.results;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pba.models.pokemon.Pokemon;
import pba.utils.PokemonDataUtils;

import java.text.DecimalFormat;

@Getter
@NoArgsConstructor
public class ResultsData {

  @CsvBindByPosition(position = 0)
  @CsvBindByName(column = "Trainer", required = true)
  private String trainer;

  @CsvBindByPosition(position = 1)
  @CsvBindByName(column = "Pokemon", required = true)
  private String pokemon;

  @CsvBindByPosition(position = 2)
  @CsvBindByName(column = "Games Played", required = true)
  private int totalGamesPlayed = 0;

  @CsvBindByPosition(position = 3)
  @CsvBindByName(column = "Knockouts", required = true)
  private int totalKnockOuts = 0;

  @CsvBindByPosition(position = 4)
  @CsvBindByName(column = "Deaths", required = true)
  private int totalDeaths = 0;

  @CsvBindByPosition(position = 5)
  @CsvBindByName(column = "Total dmg dealt", required = true)
  private int totalDamageDealt = 0;

  @CsvBindByPosition(position = 6)
  @CsvBindByName(column = "Total dmg taken", required = true)
  private int totalDamageTaken = 0;

  @CsvBindByPosition(position = 7)
  @CsvBindByName(column = "KO's per game", required = true)
  private double knockOutsPerGame = 0.0;

  @CsvBindByPosition(position = 8)
  @CsvBindByName(column = "Dmg dealt per game", required = true)
  private double damageDealtPerGame = 0.0;

  @CsvBindByPosition(position = 9)
  @CsvBindByName(column = "Dmg taken per game", required = true)
  private double damageTakenPerGame = 0.0;

  public ResultsData(Pokemon pokemon) {
    this.trainer = pokemon.getTrainer().getName();
    this.pokemon = pokemon.getName();
  }

  public void update(Pokemon pokemon) {
    this.totalGamesPlayed += 1;
    this.totalKnockOuts += pokemon.getKnockouts();
    this.totalDamageDealt += pokemon.getDamageDealt();
    this.totalDamageTaken += pokemon.getDamageTaken();
    this.totalDeaths += (pokemon.getCurrentHealth() == 0) ? 1 : 0;
  }

  public ResultsData calculatePerGameStats() {
    DecimalFormat df = PokemonDataUtils.getDecimalFormatter();
    this.knockOutsPerGame = Double.parseDouble(df.format((double) totalKnockOuts / totalGamesPlayed));
    this.damageDealtPerGame = Double.parseDouble(df.format((double) totalDamageDealt / totalGamesPlayed));
    this.damageTakenPerGame = Double.parseDouble(df.format((double) totalDamageTaken / totalGamesPlayed));
    return this;
  }
}
