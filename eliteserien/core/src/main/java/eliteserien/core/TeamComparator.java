package eliteserien.core;

import java.io.Serializable;
import java.util.Comparator;

public class TeamComparator implements Comparator<SoccerTeam>, Serializable {

    public TeamComparator() {

    }

    public int compare(SoccerTeam t1, SoccerTeam t2) {
// logikk for å sammenligne to lag t1 og t2 basert på antall kamper vunnet, returnerer positivt tall dersom t1 er "størst" og negativt dersom t2 er "størst", 0 ellers.
        return 0;
    }

}