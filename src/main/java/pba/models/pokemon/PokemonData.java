package pba.models.pokemon;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pba.utils.PokemonDataUtils;

import java.text.DecimalFormat;

@Getter
@NoArgsConstructor
public class PokemonData {

  @CsvBindByName(column = "Trainer and Pokemon", required = true)
  private String trainerAndPokemon;

  @CsvBindByName(column = "Games Played", required = true)
  private int totalGamesPlayed = 0;

  @CsvBindByName(column = "Knockouts", required = true)
  private int totalKnockOuts = 0;

  @CsvBindByName(column = "Deaths", required = true)
  private int totalDeaths = 0;

  @CsvBindByName(column = "Total dmg dealt", required = true)
  private int totalDamageDealt = 0;

  @CsvBindByName(column = "Total dmg taken", required = true)
  private int totalDamageTaken = 0;

  @CsvBindByName(column = "KO's per game", required = true)
  private double knockOutsPerGame = 0.0;

  @CsvBindByName(column = "dmg dealt per game", required = true)
  private double damageDealtPerGame = 0.0;

  @CsvBindByName(column = "dmg taken per game", required = true)
  private double damageTakenPerGame = 0.0;

  public PokemonData(String trainerAndPokemon) {
    this.trainerAndPokemon = trainerAndPokemon;
  }

  public void update(Pokemon pokemon) {
    this.totalGamesPlayed += 1;
    this.totalKnockOuts += pokemon.getKnockouts();
    this.totalDamageDealt += pokemon.getDamageDealt();
    this.totalDamageTaken += pokemon.getDamageTaken();
    this.totalDeaths += (pokemon.getCurrentHealth() == 0) ? 1 : 0;
    this.knockOutsPerGame = Math.floor(((double) totalKnockOuts / totalGamesPlayed) * 100 / 100);
    this.damageDealtPerGame = Math.floor(((double) totalDamageDealt / totalGamesPlayed) * 100 / 100);
    this.damageTakenPerGame = Math.floor(((double) totalDamageTaken / totalGamesPlayed) * 100 / 100);
  }

  public PokemonData calculatePerGameStats() {
    DecimalFormat df = PokemonDataUtils.getDecimalFormatter();
    this.knockOutsPerGame = Double.parseDouble(df.format((double) totalKnockOuts / totalGamesPlayed));
    this.damageDealtPerGame = Double.parseDouble(df.format((double) totalDamageDealt / totalGamesPlayed));
    this.damageTakenPerGame = Double.parseDouble(df.format((double) totalDamageTaken / totalGamesPlayed));
    return this;
  }
}
