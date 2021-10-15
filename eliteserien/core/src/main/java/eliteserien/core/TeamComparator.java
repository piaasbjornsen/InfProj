package eliteserien.core;

import java.io.Serializable;
import java.util.Comparator;

/**
 * TeamComparator class for comparing football teams based on points.
 */

public class TeamComparator implements Comparator<Team>, Serializable {

    /**
    * compare: Returns positive number if team 2 has more points than team 1.
    */

    public int compare(Team t1, Team t2) {
        return t2.getPoints() - t1.getPoints();
    }
}