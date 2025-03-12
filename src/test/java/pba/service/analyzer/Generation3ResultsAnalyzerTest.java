package pba.service.analyzer;

import java.util.List;
import org.junit.jupiter.api.Test;

class Generation3ResultsAnalyzerTest {

  @Test
  void itShouldAnalyzeResults() {

    Generation3ResultsAnalyzer generation3ResultsAnalyzer = new Generation3ResultsAnalyzer();

    generation3ResultsAnalyzer.analyze(List.of());
  }
}
