package pba.models.replay.results;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Generation3Results extends ReplayResult {

  private List<ResultsData> results = new ArrayList<>();
}
