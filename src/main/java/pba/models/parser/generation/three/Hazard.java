package pba.models.parser.generation.three;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum Hazard {

  SPIKES("Spikes");

  private final String name;
  private static final Map<String, Hazard> ENUM_MAP;

  static {
    Map<String, Hazard> nameToHazard = new HashMap<>();
    Arrays.stream(Hazard.values())
        .forEach(hazard -> nameToHazard.put(hazard.getName().toLowerCase(), hazard));
    ENUM_MAP = Collections.unmodifiableMap(nameToHazard);
  }

  Hazard(String name) {
    this.name = name;
  }

  public static Hazard get(String name) {
    return ENUM_MAP.getOrDefault(name.toLowerCase(), null);
  }
}
