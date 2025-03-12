package pba.service.analyzer;

import org.junit.jupiter.api.Test;

import java.util.List;

class Generation3ResultsAnalyzerTest {


    @Test
    void itShouldAnalyzeResults() {

        Generation3ResultsAnalyzer generation3ResultsAnalyzer = new Generation3ResultsAnalyzer();

        generation3ResultsAnalyzer.analyze(List.of());

    }



}
