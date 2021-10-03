package eliteserien.core;


import java.util.Collection;

/**
 * Core data of soccerteams
 * Attributes: name and points
 * Constructor: one with empty input(gives a soccerteam with name: "", points: 0), and one with input name and points.
 * Methods: getters and setters, addPoints and private method for valid name check (must be team in tippeligaen).
 */

public class SoccerTeam{

    private String name;
    private int points;
    private Collection<String> validNames; 
    /** 
     * TODO: make validname-list
     */

    public SoccerTeam(){
    }

    public SoccerTeam(String name, int points) {
        isValidName(name);
        this.name = name;
        this.points = points;
    }

    @Override
    public String toString() {
        String soccerteamString = getName() + ", " + getPoints();
        return soccerteamString;
    }
    
/**
 *  TODO: make isValidName check if the input name is in the collection of valid names. Add exception if name not valid.
 */

    private boolean isValidName(String name) {
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        isValidName(name);
        this.name = name;
    }

    public int getPoints(){
        return points;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public void addPoints(int points){
        this.points += points;
    }


}
