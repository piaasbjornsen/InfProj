package eliteserien.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Table extends AbstractTable {

    private List<SoccerTeam> teams = new ArrayList<>(); //typen Team viser til klassen for lagene og må endre navn til hva nå enn den klassen blir kalt.

    public Table(String name, SoccerTeam... teams) {
        super(name);
        addSoccerTeams(teams);
    }

    @Override
    public String toString() {
        return String.format("[Table name=%s]", getName());
    }

    @Override
    public SoccerTeam createSoccerTeam() {
        return new TableTeam();
    }

    @Override
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

    @Override
    public void removeSoccerTeam(SoccerTeam team) {
        teams.remove(team);
    }

    @Override
    public int indexOf(SoccerTeam team) {
        return teams.indexOf(team);
    }

    @Override
    public void moveSoccerTeam(SoccerTeam team, int newIndex) {
        teams.remove(team);
        teams.add(newIndex, team);
        fireTableChanged();
    }

    @Override
    protected void fireTableChanged(MatchListener listener) {
        listener.tableChanged(this);
    }

    private class TableTeam extends SoccerTeam {

        Table getTable() {
            return Table.this;
        }


        @Override
        public void setName(String name) {
            if (!Objects.equals(name, getName())) {
                super.setName(name);
                fireTableChanged(this);
            }
        }

        @Override
        public void setPoints(int points) {
            super.setPoints(points);
            fireTableChanged(this);
        }

        @Override
        public void addPoints(int points) {
            super.addPoints(points);
            fireTableChanged(this);
        }
    }
}