package pba.parser;

import pba.models.replay.Replay;
import pba.models.replay.results.ReplayResult;

import java.util.List;

public abstract class ReplayParser<T extends ReplayResult> {

  public abstract T parseReplays(List<Replay> replay);
}
