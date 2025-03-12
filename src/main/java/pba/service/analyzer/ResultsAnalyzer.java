package pba.service.analyzer;

import pba.models.replay.data.GenerationData;
import pba.models.replay.results.ReplayResult;

import java.util.List;

public interface ResultsAnalyzer<T extends GenerationData, R extends ReplayResult> {

   R analyze(List<T> replayResults);
}
