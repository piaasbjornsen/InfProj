package eliteserien.core;

public class Tabell implements MatchListener{

    private List<Team> teams; //typen Team viser til klassen for lagene og må endre navn til hva nå enn den klassen blir kalt.

    public Tabell(Team... teams){
        this.teams = new ArrayList<Team>(Arrays.asList(teams));
    }

}