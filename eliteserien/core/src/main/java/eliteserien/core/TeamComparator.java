package eliteserien.core;

import java.io.Serializable;
import java.util.Comparator;


public class TeamComparator implements Comparator<Team>, Serializable {

    //Return positive number if team 2 has more points than team 1, and negative in opposite case.
    public int compare(Team t1, Team t2) {
        return t2.getPoints() - t1.getPoints();
    }
}