package pba.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Pokemon {

  private String name;
  private String nickname;
  private int totalHealth;
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

}
