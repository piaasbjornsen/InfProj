package eliteserien.core;

import java.util.ArrayList;
import java.util.Collection;

public class AbstractTable {

    private String name;

    public AbstractTable(String name){
        setName(name);
    }

    @Override
    public String toString(){
        return String.format("[AbstractTable name=%s]", getName());
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SoccerTeam createSoccerTeam(){
        throw new UnsupportedOperationException("An abstract TableList cannot create SoccerTeams");

    }

    public void addSoccerTeams(SoccerTeam...Teams){
        throw new UnsupportedOperationException("An abstract TableList cannot add SoccerTeams");
    }

    public void addSoccerTeam(SoccerTeam team){
        addSoccerTeams(team);
    }

    public void removeSoccerTeam(SoccerTeam team){
        throw new UnsupportedOperationException("An abstract TableList cannot remove SoccerTeams");
    }

    public int indexOf(SoccerTeam team){
        return -1;

    }

    public void moveSoccerTeam(SoccerTeam team, int newIndex){
        throw new UnsupportedOperationException("An abstract TableList cannot move SoccerTeams");
    }

    private Collection<MatchListener> matchListeners = new ArrayList<>();

    public void addMatchListeneer(MatchListener listener){
       matchListeners.add(listener);
    }

    public void removeMatchListnere(MatchListener listener){
        matchListeners.remove(listener);
    }

    protected void fireTableChanged(SoccerTeam team){
        fireTableChanged();
    }

    protected void fireTableChanged(){
        for(MatchListener listener : matchListeners){
            fireTableChanged(listener);
        }
    }

    protected void fireTableChanged(MatchListener listener){
    }


}
