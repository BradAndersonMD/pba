package pba.parser.generation;

import pba.models.replay.data.GenerationData;

public interface GenerationActionParser<R extends GenerationData> {

  R parseLines(String[] lines);

}
