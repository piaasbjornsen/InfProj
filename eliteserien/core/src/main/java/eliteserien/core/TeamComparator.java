package eliteserien.core;

import java.io.Serializable;
import java.util.Comparator;

/**
 * TeamComparator class for comparing teams based on points. Return positive
 * number if team 2 has more points than team 1.
 */

public class TeamComparator implements Comparator<Team>, Serializable {

    public int compare(Team t1, Team t2) {
        return t2.getPoints() - t1.getPoints();
    }
}