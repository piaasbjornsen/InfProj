package eliteserien.core;

import java.util.List;

public class Tabell implements MatchListener {

    private List<SoccerTeam> teams; //typen Team viser til klassen for lagene og må endre navn til hva nå enn den klassen blir kalt.

    public Tabell(List<SoccerTeam> teams) {
        this.teams = teams;
    }

    public List<SoccerTeam> getTeams() {
        return teams;
    }
}