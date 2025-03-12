package pba.models.parser.generation.three;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum Weather {
  SANDSTORM("Sandstorm"),
  SPIKES("Spikes");

  private final String name;
  private static final Map<String, Weather> ENUM_MAP;

  static {
    Map<String, Weather> nameToWeather = new HashMap<>();
    Arrays.stream(Weather.values())
        .forEach(weather -> nameToWeather.put(weather.getName().toLowerCase(), weather));
    ENUM_MAP = Collections.unmodifiableMap(nameToWeather);
  }

  Weather(String name) {
    this.name = name;
  }

  public static Weather get(String name) {
    return ENUM_MAP.getOrDefault(name.toLowerCase(), null);
  }
}
