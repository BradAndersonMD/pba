package pba.models.replay.results;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Generation3Results extends ReplayResult {

  private List<ResultsData> results = new ArrayList<>();
}
