package pba.parser;

import java.util.List;
import lombok.Generated;
import pba.models.replay.Replay;
import pba.models.replay.results.ReplayResult;

@Generated
public abstract class ReplayParser<T extends ReplayResult> {

  public abstract T parseReplays(List<Replay> replay);
}
