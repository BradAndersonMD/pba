package pba.service.analyzer;

import pba.models.replay.results.ReplayResults;

public interface ResultsAnalyzer<T extends ReplayResults> {

  void analyze(T replayResults);

}
