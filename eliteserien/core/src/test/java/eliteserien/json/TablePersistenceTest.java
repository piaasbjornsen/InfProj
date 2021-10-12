package eliteserien.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eliteserien.core.Table;
import eliteserien.core.Team;

public class TablePersistenceTest {
    private TablePersistence tablePersistence;
    private Table table;
    private Team team;

    @BeforeEach
    public void SetUp() {
        tablePersistence = new TablePersistence();
        team = new Team("STK", 2);
        table = new Table();
    }

    @Test
    public void testLoadInitialTable() {
        try {
            table = tablePersistence.loadInitialTable("Table_test.json");
            assertEquals(3, table.getTeams().size());
            assertEquals("GLIMT", table.getTeams().iterator().next().getName());
            assertEquals(10, table.getTeams().iterator().next().getPoints());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSaveAndLoadTable() {
        table.addTeams(team);
        try {
            tablePersistence.saveTable(table, "Table_test.json");
        } catch (IOException e) {
            fail();
        }
        try {
            Table table2 = tablePersistence.loadSavedTable("Table_test.json");
            assertEquals(1, table2.getTeams().size());
            assertEquals(team.getName(), table2.getTeams().iterator().next().getName());
            assertEquals(team.getPoints(), table2.getTeams().iterator().next().getPoints());   
        } catch (IOException e) {
            fail();
        }
    }
}
