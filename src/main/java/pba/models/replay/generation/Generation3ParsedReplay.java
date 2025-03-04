package pba.models.replay.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pba.models.Pokemon;
import pba.models.replay.ParsedReplay;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Generation3ParsedReplay implements ParsedReplay {

    private Map<String, List<Pokemon>> playerToPokemon;
}
