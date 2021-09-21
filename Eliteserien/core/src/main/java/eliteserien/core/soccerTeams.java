
public class soccerTeams{

    String name;
    int playedGames;
    int lostGames;
    int wonGames;
    int tiedGames;

    public soccerTeams(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getLostGames() {
        return lostGames;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public int getTiedGames() {
        return tiedGames;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void setLostGames(int lostGames) {
        this.lostGames = lostGames;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    public void setTiedGames(int tiedGames) {
        this.tiedGames = tiedGames;
    }

    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }
}
