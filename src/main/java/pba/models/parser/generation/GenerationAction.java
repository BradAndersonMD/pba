package pba.models.parser.generation;

import java.util.List;

public interface GenerationAction {

  List<String> getIneligibleActions();

  List<String> getEligibleActions();

  boolean isEligibleAction();
}
