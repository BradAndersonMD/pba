package pba.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PokemonDataUtils {

  public static DecimalFormat getDecimalFormatter() {
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    decimalFormat.setRoundingMode(RoundingMode.FLOOR);
    return decimalFormat;
  }
}
