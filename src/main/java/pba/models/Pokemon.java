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
  private String item;
  // Tracks the status and the Pokémon that caused it
  private Pair<StatusEffect, Pokemon> statusEffectAndPokemon;
  private int currentHealth = 100;
  private int totalHealth = 100;
  private int damageTaken;
  private int damageDealt;
  private int knockouts;

  public void takeDamage(int damage){
    this.currentHealth = this.currentHealth - damage;
  }

  public void dealDamage(int damage){
    this.damageDealt += damage;
  }

  public void update(Pokemon pokemon) {
    this.trainer = pokemon.trainer;
    this.name = pokemon.name;
    this.nickname = pokemon.nickname;
    this.ability = pokemon.ability;
    this.uuid = pokemon.uuid;
    this.currentHealth = pokemon.currentHealth;
    this.totalHealth = pokemon.totalHealth;
    this.damageTaken = pokemon.damageTaken;
    this.damageDealt = pokemon.damageDealt;
    this.knockouts = pokemon.knockouts;
  }

}
