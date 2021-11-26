package sportstable.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing the methods in the Team class
 */

public class TeamTest {
    private Team team1;
    private Team team2;

    /**
     * Before each test, SetUp creates the table and the teams that we want to use
     * in them
     */

    @BeforeEach
    public void setUp() {
        this.team1 = new Team("ARK", 0);
        this.team2 = new Team("LSK", 5);
    }

    /**
     * Test for setTeamName method Uses setName method to set names to the teams
     * from setUp Then compares the name to the teams with the name we gave them
     */

    @Test
    public void testSetTeamName() {
        team1.setName("RBK");
        assertEquals("RBK", team1.getName());
        assertEquals("LSK", team2.getName());
        team2.setName("GLIMT");
        assertEquals("GLIMT", team2.getName());
    }

    /**
     * Test for setPoints method Uses setPoints method to set points to the teams
     * from setUp Then compares the points to the teams with the point we gave them
     */

    @Test
    public void testSetPoints() {
        team1.setPoints(3);
        ;
        assertEquals(3, team1.getPoints());
        assertEquals(5, team2.getPoints());
        team2.setPoints(7);
        assertEquals(7, team2.getPoints());
    }

    /**
     * Test for addPoints method Uses addPints method to add points to the teams
     * from setUp Then compares the points to the teams with the point we gave them
     */

    @Test
    public void testAddPoints() {
        team1.addPoints(3);
        assertEquals(3, team1.getPoints());
        assertEquals(5, team2.getPoints());
        team2.addPoints(2);
        assertEquals(7, team2.getPoints());
    }
}
