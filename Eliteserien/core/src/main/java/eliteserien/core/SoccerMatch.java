package eliteserien.core;

public class Match {
    private String team1name;
    private String team2name;
    private int team1points;
    private int team2points;

    /*
     * ArrayList<String> format: [teamname, teamscore]
     */

    public Match() {
    }

    public Match(String team1name, String team2name, int team1points, int team2points) {
        this.team1name = team1name;
        this.team2name = team2name;
        this.team1points = team1points;
        this.team2points = team2points;
    }

    public String getTeam1Name() {
        return team1name;
    }

    public String getTeam2Name() {
        return team2name;
    }

    public int getTeam1Points() {
        return team1points;
    }

    public int getTeam2Points() {
        return team2points;
    }

    public void setTeam1Name(String team1name) {
        this.team1name = team1name;
    }

    public void setTeam2Name(String team2name) {
        this.team2name = team2name;
    }

    public void setTeam1Points(int team1points) {
        this.team1points = team1points;
    }
    
    public void setTeam2Points(int team2points) {
        this.team2points = team2points;
    }

    
}
