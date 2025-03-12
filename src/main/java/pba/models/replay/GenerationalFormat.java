package pba.models.replay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum GenerationalFormat {
  @JsonProperty("gen3ou")
  GEN_3_OU("3", "ou");
  private final String generation;
  private final String format;

  GenerationalFormat(String generation, String format) {
    this.generation = generation;
    this.format = format;
  }
}
