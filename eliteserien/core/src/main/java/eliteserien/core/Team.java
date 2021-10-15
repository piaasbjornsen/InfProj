package eliteserien.core;

/**
 * Team class for core data of football teams in Tippeligaen
*/

public class Team{

    /**
    * Attributes: name and points
    */

    private String name;
    private int points;

    /** 
    * Empty contructor: Creates a football team with name: "", points: 0
    */
    public Team(){
    }

    /** 
    * Constructor: Creates a football team accoring to input name and input points
    * @param football team name
    * @param points
    */

    public Team(String name, int points) {
        this.name = name;
        this.points = points;
    }

    /**
    * toString: Returns a string of the team: "name, points"
    * This is not optimal, since team should be a TableView object so the data is not static
    * @return "team name, points"
    */

    @Override
    public String toString() {
        String teamString = getName() + ", " + getPoints();
        return teamString;
    }

    /**
    * getName: Returns name of Team object
    * @return Name of football team
    */

    public String getName() {
        return name;
    }

    /**
    * setName: Sets name of Team object
    * @param name of football team
    */

    public void setName(String name){
        this.name = name;
    }

    /** 
    * getPoints: Returns number of points (int) of Team object
    * @return points (int)
    */

    public int getPoints(){
        return points;
    }

    /**
    * setPoints: Sets number of points to a Team object
    * @param points (int)
    */

    public void setPoints(int points){
        this.points = points;
    }

    /** 
    * addPoints: Adds points to a Team object. Already existing points + new points
    * @param points (int)
    */

    public void addPoints(int points){
        this.points += points;
    }
}
