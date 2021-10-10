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
 * Constructor: takes Team objects as input and adds them in the collection of teams.
 * 
 * Methods: 
 * toString-method showing the teams in the list and their points. 
 * Getters and setters for name and teams.
 * CreateTeam method which creates a Tableteam object (extends Team).
 * AddTeam method which takes Team objects as input, makes new TableTeam objects with same 
 * attributes as input Team objects and adds the TableTeam objects to the list of teams.
 * SortTable method uses the TeamComparator class and built-in sorting functions for Collections
 * to sort teams in table by numbers. Team with most points gets placed first in the list. 
 * Iterator method go through all teams in Table. 
 * 
 * Listeners:
 * Listeners will be used in later iteration of project, when implementation of matches is added.
 * This class have a collection of listeners with methods for adding and removing listeners. 
 * fireTableChange method tells the listeners in the collection that the Table has been changed.
 * 
 * The private class TableTeam is an extention of the Team class and is implemented to
 * tell the table object and the listeners of this object that the table
 * has changed when the attributes of the team is changed.
 */


public class Table {
    private String name = "Eliteserien";
    private List<Team> teams = new ArrayList<>();

    public Table(Team... teams) {
        addTeams(teams);
    }

    @Override
    public String toString() {
        String teamListString = "";
        for (Team team : teams) {
            teamListString = teamListString.concat(team.toString());
            teamListString = teamListString.concat("\n");
        }
        return teamListString;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Table getTable(String name) {
        return this;
    }

    public Team createTeam(String name, int points) {
        return new TableTeam(name, points);
    }

    private void sortTable() {
        Collections.sort(this.teams, new TeamComparator());
    }

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
        fireTableChanged();
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

    private Collection<TableListener> tableListeners = new ArrayList<>();

    public void addTableListener(TableListener listener) {
        tableListeners.add(listener);
    }

    public void removeTableListener(TableListener listener) {
        tableListeners.remove(listener);
    }

    protected void fireTableChanged(Table table) {
        fireTableChanged();
    }

    protected void fireTableChanged(TableListener listener) {
        listener.tableChanged(this);
    }
    
    protected void fireTableChanged() {
        for (TableListener listener : tableListeners) {
            fireTableChanged(listener);
        }
    }


    private class TableTeam extends Team {


        public TableTeam(String name, int points) {
            super(name, points);
        }

        Table getTable() {
            return Table.this;
        }

        @Override
        public void setName(String name) {
            super.setName(name);
            fireTableChanged(Table.this);
        }

        @Override
        public void setPoints(int points) {
            super.setPoints(points);
            fireTableChanged(Table.this);
        }

        @Override
        public void addPoints(int points) {
            super.addPoints(points);
            fireTableChanged(Table.this);
        }
    }
}