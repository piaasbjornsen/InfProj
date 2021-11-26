package sportstable.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class to test that methods in Table class works as planned
 */

public class TableTest {
    private Table table;
    private Team team1;
    private Team team2;
    private Team team3;

    /**
     * Before each test, SetUp creates the table and the teams that we want to use
     * in them
     */

    @BeforeEach
    public void setUp() {
        table = new Table();
        team1 = new Team("RBK", 12);
        team2 = new Team("GLIMT", 13);
        team3 = new Team("MOLDE", 10);
    }

    /**
     * Test for getName method Checks that the table is called Eliteserien
     */

    @Test
    public void testGetName() {
        assertEquals("Eliteserien", table.getName());
    }

    /**
     * Tests that the setName methods changes the Table name
     */

    @Test
    public void testSetName() {
        table.setName("Skoleturnering");
        assertEquals("Skoleturnering", table.getName());
    }

    /**
     * Tests that AddTeam method adds a Team object with correct name and points
     */

    @Test
    public void testAddTeam() {
        table.addTeams(team1); // Adds team1 that was initalized in setUp to table (which was also initialized
                               // in setUp)
        assertTrue(table.iterator().hasNext()); // Checks that the table has an object (team1)
        Team addedTeam = table.iterator().next();

        // Checks then that the team in the table is the same as we wanted to add
        // (team1), by comparing their name and points
        assertEquals(team1.getName(), addedTeam.getName());
        assertTrue(team1.getPoints() == addedTeam.getPoints());

        // Then adds two more teams, and check if the table now has 3 teams
        table.addTeams(team2, team3);
        assertTrue(table.getTeams().size() == 3);
    }

    /**
     * Tests that the sortTable method sorts the teams by points
     */

    @Test
    public void testSortTable() {
        table.addTeams(team1, team2, team3); // Adds 3 teams to the table from setUp()
        int i = 100;
        for (Team team : table.getTeams()) { // Tterates thorugh the teams
            assertTrue(i >= team.getPoints()); // Checks that the the first team in table has more points than the one
                                               // after, and so on
            i = team.getPoints();
        }
    }

    /**
     * Test for testGetTeams_emptyList() Checks that the table has no teams
     */

    @Test
    public void testGetTeams_emptyList() {
        assertTrue(table.getTeams().isEmpty());
    }
}
