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
     * toString-method: Shows the teams in the list and their points.
     * Each teams should preferable be a TableView object rather than  a string
     * @return list of team strings
    */

    @Override
    public String toString() {
        String teamListString = "";
        for (Team team : teams) {
            teamListString = teamListString.concat(team.toString());
            teamListString = teamListString.concat("\n");
        }
        return teamListString;
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
    * createTeam: Creates a Tableteam object (extends Team)
    * @param team name, points
    * @return a new TableTeam object
    */

    public Team createTeam(String name, int points) {
        return new TableTeam(name, points);
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
            TableTeam tableTeam = null;
            if (team instanceof TableTeam tt) {
                tableTeam = tt;
            } else {
                tableTeam = new TableTeam(team.getName(), team.getPoints());
            }
            if (tableTeam.getTable() != this) {
                throw new IllegalStateException("TableTeam does not belong to this list Table");
            }
            this.teams.add(tableTeam);
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

    /**
    * private class TableTeam: is an extention of the Team class 
    * and is implemented to tell the table object and the listeners of this object 
    * that the table has changed when the attributes of the team is changed.
    */

    private class TableTeam extends Team {

        public TableTeam(String name, int points) {
            super(name, points);
        }

        Table getTable() {
            return Table.this;
        }
    }
}