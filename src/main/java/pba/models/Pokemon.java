package pba.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Pokemon {

  private Trainer trainer;
  private String name;
  private String nickname;
  private String ability;
  private String uuid = UUID.randomUUID().toString();
  private int currentHealth = 100;
  private int totalHealth = 100;
  private int damageTaken;
  private int damageDealt;
  private int deaths;
  private int knockouts;

  public int getHealth() {
    return totalHealth - damageTaken;
  }

  public void setTotalHealth() {
    this.totalHealth -= damageTaken;
  }


  public Pokemon update(Pokemon pokemon){
    this.trainer = pokemon.trainer;
    this.name = pokemon.name;
    this.nickname = pokemon.nickname;
    this.ability = pokemon.ability;
    this.uuid = pokemon.uuid;
    this.currentHealth = pokemon.currentHealth;
    this.totalHealth = pokemon.totalHealth;
    this.damageTaken = pokemon.damageTaken;
    this.damageDealt = pokemon.damageDealt;
    this.deaths = pokemon.deaths;
    this.knockouts = pokemon.knockouts;
    return this;
  }

}
