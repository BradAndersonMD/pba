package pba.service.analyzer;

import java.util.List;
import pba.models.replay.data.GenerationData;
import pba.models.replay.results.ReplayResult;

public interface ResultsAnalyzer<T extends GenerationData, R extends ReplayResult> {

  R analyze(List<T> replayResults);
}
