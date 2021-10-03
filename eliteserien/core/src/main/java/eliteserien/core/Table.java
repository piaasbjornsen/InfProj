package eliteserien.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Core data for Table class.
 * 
 * Attributes: name: "Tippeligaen", list: ArrayList of SoccerTeam objects. 
 * 
 * Constructor: takes SoccerTeam objects as input and adds them in the collection of teams.
 * 
 * Methods: 
 * toString-method showing the teams in the list and their points. 
 * Getters and setters for name and teams.
 * CreateSoccerTeam method which creates a Tableteam object (extends SoccerTeam).
 * AddSoccerTeam method which takes SoccerTeam objects as input, makes new TableTeam objects with same 
 * attributes as input SoccerTeam objects and adds the TableTeam objects to the list of teams.
 * other methods: moveSoccerTeam, removeSoccerTeam, indexOf(SoccerTeam) and iterator.
 * 
 * This class also have a collection of listeners with methods for adding and removing listeners. 
 * fireTableChange method tells the listeners in the collection that the Table has been changed.
 * 
 * The private class TableTeam is an extention of the SoccerTeam class and is implemented to
 * tell the table object and the listeners of this object that the table
 * has changed when the attributes of the team is changed.
 */


public class Table {
    private String name = "Tippeligaen";
    private List<SoccerTeam> teams = new ArrayList<>();

    public Table(SoccerTeam... teams) {
        addSoccerTeams(teams);
    }

    @Override
    public String toString() {
        String teamListString = "";
        for (SoccerTeam team : teams) {
            teamListString = teamListString + team.toString();
        }
        return String.format("[Table text=%s %n]", teamListString);
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

    public SoccerTeam createSoccerTeam() {
        return new TableTeam();
    }

    public void addSoccerTeams(SoccerTeam... teams) throws IllegalStateException {
        for (SoccerTeam team : teams) {
            TableTeam tableTeam = null;
            if (team instanceof TableTeam tt) {
                tableTeam = tt;
            } else {
                tableTeam = new TableTeam();
                tableTeam.setName(team.getName());
                tableTeam.setPoints(team.getPoints());
            }
            if (tableTeam.getTable() != this) {
                throw new IllegalStateException("TableTeam does not belong to this list Table");
            }
            this.teams.add(tableTeam);
        }
        fireTableChanged();
    }

    public void removeSoccerTeam(SoccerTeam team) {
        teams.remove(team);
    }

    public Iterator<SoccerTeam> iterator() {
        return teams.iterator();
    }

    public Collection<SoccerTeam> getSoccerTeams() {
        Collection<SoccerTeam> soccerTeams = new ArrayList<>(teams.size());
        for (SoccerTeam team : teams) {
            soccerTeams.add(team);
        }
        return soccerTeams;
    }

    public int indexOf(SoccerTeam team) {
        return teams.indexOf(team);
    }

    public void moveSoccerTeam(SoccerTeam team, int newIndex) {
        teams.remove(team);
        teams.add(newIndex, team);
        fireTableChanged();
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


    private class TableTeam extends SoccerTeam {

        Table getTable() {
            return Table.this;
        }


        @Override
        public void setName(String name) {
            if (!Objects.equals(name, getName())) {
                super.setName(name);
                fireTableChanged(Table.this);
            }
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