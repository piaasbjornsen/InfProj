package eliteserien.core;

import java.io.Serializable;
import java.util.Comparator;

/** 
 * Unused class for now. Will be used in later steps of the project for TableTeam sorting. 
 */

public class TeamComparator implements Comparator<SoccerTeam>, Serializable {

    public TeamComparator() {

    }

    public int compare(SoccerTeam t1, SoccerTeam t2) {
// logic for comparing two teams based on points. Return positive number if team 1 has more points than team 2, and negative in opposite case.
        return 0;
    }
}