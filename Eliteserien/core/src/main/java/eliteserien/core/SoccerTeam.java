package eliteserien.core;

public class SoccerTeam{

    private String name;
    private int points;

    public SoccerTeam(){
    }

    public String getName() {
        return name;
    }

    public int getPoints(){
        return points;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public void addPoints(int points){
        this.points += points;
    }


}
