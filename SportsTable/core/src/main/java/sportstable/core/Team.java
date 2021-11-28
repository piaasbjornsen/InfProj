package sportstable.core;

/**
 * Team class for core data of teams in SportsTable
 */

public class Team {

    private String name; // Team name
    private int points; // Points to team

    /**
     * Creates a sports team according to input name and input points
     *
     * @param Team   name
     * @param points teampoints
     */

    public Team(String name, int points) {
        this.name = name;
        this.points = points;
    }

    /**
     * @return Team name
     */

    public String getName() {
        return name;
    }

    /**
     * Sets name of Team object
     *
     * @param Team name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Points to Team object
     */

    public int getPoints() {
        return points;
    }

    /**
     * Sets number of points to a Team object
     *
     * @param Points to a Team object
     */

    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Adds points to a Team object. Already existing points + new points
     *
     * @param Points to be added to a Team object
     */

    public void addPoints(int points) {
        this.points += points;
    }
}
