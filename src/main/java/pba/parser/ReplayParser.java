package pba.parser;

import pba.models.replay.Replay;
import pba.models.replay.results.ReplayResults;

public interface ReplayParser<T extends ReplayResults> {

    T parse(Replay replay);

}
