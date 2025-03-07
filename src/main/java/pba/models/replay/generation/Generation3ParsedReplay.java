package pba.models.replay.generation;

import lombok.Getter;
import pba.models.Pokemon;
import pba.models.replay.ParsedReplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Generation3ParsedReplay implements ParsedReplay {

    private Map<String, List<Pokemon>> playerToPokemon = new HashMap<>();
}
