package pba.models.pokemon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pba.models.Pokemon;

@Getter
@NoArgsConstructor
public class PokemonData {

    private String trainerAndPokemon;
    private int totalGamesPlayed = 0;
    private int totalKnockOuts = 0 ;
    private int totalDeaths = 0;
    private int totalDamageDealt = 0;
    private int totalDamageTaken = 0;
    private double knockOutsPerGame = 0.0;
    private double damageDealtPerGame = 0.0;
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
