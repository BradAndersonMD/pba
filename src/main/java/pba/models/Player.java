package pba.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Player {

    private final List<Pokemon> roster = new ArrayList<>();
    private final String name;
    private final String shortHandName;

    public Player(String name, String shortHandName){
        this.name = name;
        this.shortHandName = shortHandName;
    }

    public void addToRoster(Pokemon pokemon){
        roster.add(pokemon);
    }

}
