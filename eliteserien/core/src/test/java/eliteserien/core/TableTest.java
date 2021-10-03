package eliteserien.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TableTest {
    private Table table;
    private Team team1;
    private Team team2;
    private Team team3;

    @BeforeEach
    public void setUp() {
        table = new Table();
        team1 = table.createTeam("RBK", 10);
        team2 = table.createTeam("GLIMT", 12);
        team3 = table.createTeam("MOLDE", 14);
    }

    @Test
    public void testGetName() {
        assertEquals("Eliteserien", table.getName());
    }

    @Test
    public void testAddTeam() {
        table.addTeams(team1);
        assertTrue(table.iterator().hasNext());
        Team addedTeam = table.iterator().next();
        assertEquals(team1.getName(), addedTeam.getName());
        assertTrue(team1.getPoints() == addedTeam.getPoints());
        table.addTeams(team2, team3);
        assertTrue(table.getTeams().size() == 3);
    }

    @Test
    public void testRemoveTeam() {
        table.addTeams(team1);
        table.removeTeam(team1);
        assertTrue(table.getTeams().isEmpty());
        table.addTeams(team2, team3);
        table.removeTeam(team3);
        assertEquals(team2, table.iterator().next());
    }

    @Test
    public void testIndexOf() {
        table.addTeams(team1, team2);
        assertEquals(0, table.indexOf(team1));
        assertEquals(1, table.indexOf(team2));
    }

    @Test
    public void testMoveTeam() {
        table.addTeams(team1, team2, team3);
        table.moveTeam(team1, 1);
        assertEquals(1, table.indexOf(team1));
        assertEquals(0, table.indexOf(team2));
    }

    @Test
    public void testGetTeams_emptyList() {
      assertTrue(table.getTeams().isEmpty());
    }
}
