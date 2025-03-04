package pba.parser.generation;

import pba.models.parser.generation.GenerationAction;
import pba.models.replay.results.ReplayResults;

public interface GenerationActionParser<T extends GenerationAction, R extends ReplayResults> {

    void applyAction(T action);

    R retrieveResults();

}
