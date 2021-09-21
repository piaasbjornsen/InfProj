package eliteserien.core;

public class SoccerTeam{

    private String name;
    private int gamesPlayed;
    private int gamesLost;
    private int gamesWon;
    private int gamesTied;

    public SoccerTeam(String name){
        this.name = name;
        this.gamesPlayed = 0;
        this.gamesTied = 0;
        this.gamesLost = 0;
        this.gamesWon = 0;
    }

    public String getName() {
        return name;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getGamesTied() {
        return gamesTied;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public void setGamesTied(int gamesTied) {
        this.gamesTied = gamesTied;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
}
