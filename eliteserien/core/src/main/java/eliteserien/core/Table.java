package eliteserien.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Core data for Table class.
 * 
 * Attributes: name: "Tippeligaen", list: ArrayList of Team objects.
 * 
 * Constructor: takes Team objects as input and adds them in the collection of
 * teams.
 * 
 * Methods: toString-method showing the teams in the list and their points.
 * Getters and setters for name and teams. CreateTeam method which creates a
 * Tableteam object (extends Team). AddTeam method which takes Team objects as
 * input, makes new TableTeam objects with same attributes as input Team objects
 * and adds the TableTeam objects to the list of teams. SortTable method uses
 * the TeamComparator class and built-in sorting functions for Collections to
 * sort teams in table by numbers. Team with most points gets placed first in
 * the list. Iterator method go through all teams in Table.
 * 
 * The private class TableTeam is an extention of the Team class and is
 * implemented to tell the table object and the listeners of this object that
 * the table has changed when the attributes of the team is changed.
 */

public class Table {
    private String name = "Eliteserien";
    private List<Team> teams = new ArrayList<>();

    public Table(Team... teams) {
        addTeams(teams);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Table getTable(String name) {
        return this;
    }

    private void sortTable() {
        Collections.sort(this.teams, new TeamComparator());
    }

    public void addTeams(Team... teams) throws IllegalStateException {
        for (Team team : teams) {
            this.teams.add(team);
            sortTable();
        }
    }

    public Iterator<Team> iterator() {
        return teams.iterator();
    }

    public Collection<Team> getTeams() {
        Collection<Team> teamcollection = new ArrayList<>(teams.size());
        for (Team team : teams) {
            teamcollection.add(team);
        }
        return teamcollection;
    }
}