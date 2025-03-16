package pba.utils;

import lombok.experimental.UtilityClass;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Utility class for writing "pretty"/rounded {@link Double}'s
 */
@UtilityClass
public class PokemonDataUtils {

  public static DecimalFormat getDecimalFormatter() {
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    decimalFormat.setRoundingMode(RoundingMode.FLOOR);
    return decimalFormat;
  }
}
