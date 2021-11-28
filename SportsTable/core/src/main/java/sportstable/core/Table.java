package sportstable.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Table class for core data to SportsTable.
 */

public class Table {

    private String name = "Eliteserien"; // Default name to table
    private List<Team> teams = new ArrayList<>(); // Arraylist of Team objects

    /**
     * Takes Team objects as input and adds them in the collection of teams.
     *
     * @param A collection of teams
     */

    public Table(Team... teams) {
        addTeams(teams);
    }

    /**
     * @return Table name
     */

    public String getName() {
        return name;
    }

    /**
     * Sets the name of table
     *
     * @param Table name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param Name of table
     * @return Requested table
     */

    public Table getTable(String name) {
        return this;
    }

    /**
     * @param Team name
     * @return Requested team, or a new team if requested team does not exist
     */

    public Team getTeam(String name) {
        for (Team team : getTeams()) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return new Team(name, 0);
    }

    /**
     * Uses the TeamComparator class and built-in sorting functions for Collections
     * to sort teams in table by numbers. Team with most points gets placed first in
     * the list.
     */

    private void sortTable() {
        Collections.sort(this.teams, new TeamComparator());
    }

    /**
     * Takes Team objects as input, and adds them to table
     *
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
     * Deletes team
     *
     * @param Team object to delete
     */

    public void deleteTeam(Team team) {
        teams.remove(team);
    }

    /**
     * Iterates through all teams in Table.
     *
     * @return iterator
     */

    public Iterator<Team> iterator() {
        return teams.iterator();
    }

    /**
     * @return A collection of the Team objects
     */

    public Collection<Team> getTeams() {
        Collection<Team> teamcollection = new ArrayList<>(teams.size());
        for (Team team : teams) {
            teamcollection.add(team);
        }
        return teamcollection;
    }
}
