package eliteserien.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Table class for core data to Tippeligaen table.
*/


public class Table {

    /** 
    * Attributes: Name of table: "Tippeligaen", teams: ArrayList of Team objects. 
    */

    private String name = "Eliteserien";
    private List<Team> teams = new ArrayList<>();

    
    /** 
    * Constructor: Takes Team objects as input and adds them in the collection of teams.
    * @param A collection of teams
    */

    public Table(Team... teams) {
        addTeams(teams);
    }


    /** 
    * getName: Returns name of table (Eliteserien)
    * @return string name
    */

    public String getName(){
        return name;
    }

    /**
    * setName: Sets the name of table (Eliteserien)
    * @param football team name
    */

    public void setName(String name) {
        this.name = name;
    }

    /**
    * @param Name of table (Eliteserien)
    * @return Eliteserien table
    */

    public Table getTable(String name) {
        return this;
    }

    /** 
    * sortTable: uses the TeamComparator class and built-in sorting functions for Collections
    * to sort teams in table by numbers. Team with most points gets placed first in the list. 
    */

    private void sortTable() {
        Collections.sort(this.teams, new TeamComparator());
    }

    /** 
    * AddTeam: Takes Team objects as input, makes new TableTeam objects with same 
    * attributes as input Team objects and adds the TableTeam objects to the list of teams.
    * @param A collection of teams
    * @throws IllegalStateException
    */

    public void addTeams(Team... teams) throws IllegalStateException {
        for (Team team : teams) {
            this.teams.add(team);
            sortTable();
        }
    }

    /**
    * iterator: Iterates through all teams in Table. 
    * @return iterator
    */

    public Iterator<Team> iterator() {
        return teams.iterator();
    }

    /**
    * @return A collection of the Team bjects
    */

    public Collection<Team> getTeams() {
        Collection<Team> teamcollection = new ArrayList<>(teams.size());
        for (Team team : teams) {
            teamcollection.add(team);
        }
        return teamcollection;
    }
}