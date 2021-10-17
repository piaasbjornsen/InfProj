package eliteserien.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        team1 = new Team("RBK", 12);
        team2 = new Team("GLIMT", 13);
        team3 = new Team("MOLDE", 10);
    }

    @Test
    public void testGetName() {
        assertEquals("Eliteserien", table.getName());
    }

    @Test
    public void testSetName() {
        table.setName("Skoleturnering");
        assertEquals("Skoleturnering", table.getName());
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
    public void testSortTable() {
        table.addTeams(team1, team2, team3);
        int i = 100;
        for (Team team : table.getTeams()) {
            assertTrue(i >= team.getPoints());
            i = team.getPoints();
        }
    }

    @Test
    public void testGetTeams_emptyList() {
        assertTrue(table.getTeams().isEmpty());
    }
}
