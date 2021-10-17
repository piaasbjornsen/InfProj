package eliteserien.core;

/**
 * Core data of football teams Attributes: name and points Constructor: one with
 * empty input(gives a football team with name: "", points: 0), and one with
 * input name and points. Methods: getters and setters and addPoints.
 */

public class Team {

    private String name;
    private int points;

    public Team(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
