package pba.models.parser.generation.three;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pba.models.parser.generation.GenerationAction;

import java.util.List;

@Getter
@AllArgsConstructor
public class Generation3Action implements GenerationAction {

  private final String actionType;
  private final String value;

  public Generation3Action(String line) {
    this.actionType =
        getIneligibleActions().stream()
            .filter(line::contains)
            .findFirst()
            .orElseGet(
                () -> getEligibleActions().stream().filter(line::contains).findFirst().orElse("|"));
    this.value = line.substring(actionType.length());
  }

  @Override
  public List<String> getIneligibleActions() {
    return List.of(
        "|j|", // joining player
        "|gametype|", // singles/double
        "|teamsize|", // team size
        "|gen|", // generation
        "|rule|", // rules for the match
        "|inactive|", // inactivity time?
        "|t:|", // Instant.ofEpochSecond()
        "|-immune|", // huh?
        "|-surestatus|", // removes a status
        "|-ability|", // Copies passive ability from another pokemon
        "|-unboost", // reduces stat
        "|turn|", // turn number
        "|-start", // TODO: Not sure what this does
        "|-fieldactivate|", // TODO: not sure what this does
        "|-resisted", // reduces damage from in-effective
        "|-sideend", // Removes hazards
        "|-singleturn", // Attack that can be interrupted
        "|-setboost|", // sets a stat to a particular stages
        "|-supereffective", // Super effective
        "|-sidestart|", // Particular side has a hazard
        "|-crit|", // which player's pokemon was crit
        "|-fail|", // ability failed
        "|-miss|", // attack missed
        "|cant|", // can't take action - slp/par/flinch??/other specific Abiltiy
        "|upkeep" // linked to weather/items? Leftovers?
        );
  }

  @Override
  public List<String> getEligibleActions() {
    return List.of(
        "|player|", // player name p1a/p2a
        "|switch|", // bringing pokemon to the field
        "|-weather|", // weather affects? linked to upkeep?
        "|move|", // which ability was selected
        "|drag|", // Removes active pokemon and pulls in random one
        "|-sideend", // Hazard removal
        "|-damage|", // which player, pokemon was damaged
        "|-status|", // tracks status - slp/par/brn/psn
        "|-heal|", // healed
        "|faint|", // KO
        "|win|" // Winner
        );
  }

  @Override
  public boolean isEligibleAction() {
    return getEligibleActions().stream().anyMatch(this.actionType::contains);
  }
}
