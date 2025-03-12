package pba.models.pokemon;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pba.models.Pokemon;

@Getter
@NoArgsConstructor
public class PokemonData {

    @CsvBindByName(column="Trainer and Pokemon", required = true)
    private String trainerAndPokemon;
    @CsvBindByName(column="Total games played", required = true)
    private int totalGamesPlayed = 0;
    @CsvBindByName(column="Total knockouts", required = true)
    private int totalKnockOuts = 0 ;
    @CsvBindByName(column="Total deaths", required = true)
    private int totalDeaths = 0;
    @CsvBindByName(column="Total damage dealt", required = true)
    private int totalDamageDealt = 0;
    @CsvBindByName(column="Total damage taken", required = true)
    private int totalDamageTaken = 0;
    @CsvBindByName(column="knock outs per game", required = true)
    private double knockOutsPerGame = 0.0;
    @CsvBindByName(column="damage dealt per game", required = true)
    private double damageDealtPerGame = 0.0;
    @CsvBindByName(column="damage taken per game", required = true)
    private double damageTakenPerGame = 0.0;

    public PokemonData(String trainerAndPokemon){
        this.trainerAndPokemon = trainerAndPokemon;
    }

    public void update(Pokemon pokemon) {
        this.totalGamesPlayed += 1;
        this.totalKnockOuts += pokemon.getKnockouts();
        this.totalDamageDealt += pokemon.getDamageDealt();
        this.totalDamageTaken += pokemon.getDamageTaken();
        this.totalDeaths += (pokemon.getCurrentHealth() == 0) ? 1 : 0;
        this.knockOutsPerGame = ((double) totalKnockOuts / totalGamesPlayed);
        this.damageDealtPerGame = ((double) totalDamageDealt / totalGamesPlayed);
        this.damageTakenPerGame = ((double) totalDamageTaken / totalGamesPlayed);
    }

}
