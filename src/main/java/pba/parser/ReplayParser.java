package pba.parser;

import java.util.List;
import pba.models.replay.Replay;

public abstract class ReplayParser {

  public abstract void parseReplays(List<Replay> replay);
}
