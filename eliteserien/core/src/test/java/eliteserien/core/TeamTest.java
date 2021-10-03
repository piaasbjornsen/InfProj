package eliteserien.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TeamTest {
    private Team team1;
    private Team team2;

    @BeforeEach
    public void setUp() {
        this.team1 = new Team();
        this.team2 = new Team("LSK", 5);
    }

    @Test
    public void testSetTeamName() {
        team1.setName("RBK");
        assertEquals("RBK", team1.getName());
        assertEquals("LSK", team2.getName());
        team2.setName("GLIMT");
        assertEquals("GLIMT", team2.getName());
    }

    @Test
    public void testSetPoints() {
        team1.setPoints(3);;
        assertEquals(3, team1.getPoints());
        assertEquals(5, team2.getPoints());
        team2.setPoints(7);
        assertEquals(7, team2.getPoints());
    }

    @Test
    public void testAddPoints() {
        team1.addPoints(3);
        assertEquals(3, team1.getPoints());
        assertEquals(5, team2.getPoints());
        team2.addPoints(2);
        assertEquals(7, team2.getPoints());
    }

    // TODO: make test for isValidName method when method is implemented.
    
}
