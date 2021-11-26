package sportstable.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sportstable.core.Table;
import sportstable.core.Team;

/**
 * Class for testing methods in the TablePersistence class
 */

public class TablePersistenceTest {
    private TablePersistence tablePersistence;
    private Table table;
    private Team team;

    /**
     * Before each test, SetUp creates the tablePersistence, the team and the table
     * that we want to use in them
     */

    @BeforeEach
    public void SetUp() {
        tablePersistence = new TablePersistence();
        team = new Team("STK", 2);
        table = new Table();
    }

    /**
     * Test for loadInitialTable method Tries to load a table from a json file
     * containing a table for testing Then checks that the loading works by checking
     * if the table contains expected teams with correct names and points
     */

    @Test
    public void testLoadInitialTable() {
        try {
            table = tablePersistence.loadInitialTable("test-Table.json");
            assertEquals(16, table.getTeams().size());
            assertEquals("GLIMT", table.getTeams().iterator().next().getName());
            assertEquals(10, table.getTeams().iterator().next().getPoints());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests that saveAndLoadTable method saves Table objects correctly and loads
     * Table objects correctly
     */

    @Test
    public void testSaveAndLoadTable() {
        table.addTeams(team); // Adds a team to table
        try {
            tablePersistence.saveTable(table, "test-Table.json"); // Saves table to a json file
        } catch (IOException e) {
            fail();
        }
        try {
            Table table2 = tablePersistence.loadSavedTable("test-Table.json"); // Loads contents from json-file into a
                                                                               // new table

            // Compares the two tables, to see if the saving and loading of the table worked
            // as expected
            assertEquals(1, table2.getTeams().size());
            assertEquals(team.getName(), table2.getTeams().iterator().next().getName());
            assertEquals(team.getPoints(), table2.getTeams().iterator().next().getPoints());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Loads the test json-file to Table object ands saves it before each test
     */

    @AfterEach
    public void cleanUp() {
        try {
            table = tablePersistence.loadInitialTable("test-Table.json");
        } catch (IOException e) {
            fail();
        }
        try {
            tablePersistence.saveTable(table, "test-Table.json");
        } catch (IOException e) {
            fail();
        }
    }
}
