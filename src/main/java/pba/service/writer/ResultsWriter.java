package pba.service.writer;

import pba.models.replay.results.ReplayResult;

public interface ResultsWriter<T extends ReplayResult> {

  void write(T results);
}
