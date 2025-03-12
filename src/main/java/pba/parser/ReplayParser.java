package pba.parser;

import java.util.List;
import pba.models.replay.Replay;
import pba.models.replay.results.ReplayResult;

public abstract class ReplayParser<T extends ReplayResult> {

  public abstract T parseReplays(List<Replay> replay);
}
