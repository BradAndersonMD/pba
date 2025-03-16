package pba.models.parser.generation.three;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum StatusEffect {
  BURN("brn"),
  POISON("psn"),
  FREEZE("frz"),
  TOXIC("tox"),
  PARALYZE("par"),
  SLEEP("slp"),
  CURSE("Curse");

  private final String name;
  private static final Map<String, StatusEffect> ENUM_MAP;

  static {
    Map<String, StatusEffect> nameToStatusEffect = new HashMap<>();
    Arrays.stream(StatusEffect.values())
        .forEach(
            statusEffect ->
                nameToStatusEffect.put(statusEffect.getName().toLowerCase(), statusEffect));
    ENUM_MAP = Collections.unmodifiableMap(nameToStatusEffect);
  }

  StatusEffect(String name) {
    this.name = name;
  }

  public static StatusEffect get(String name) {
    return ENUM_MAP.getOrDefault(name.toLowerCase(), null);
  }
}
