package eliteserien.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import eliteserien.core.Table;
import eliteserien.core.Team;
import eliteserien.json.TablePersistence;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EliteserienAppControllerTest extends ApplicationTest {

    private TablePersistence tablePersistence = new TablePersistence();
    private EliteserienAppController controller;
    private Table table;

    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("Table_test.fxml"));
        final Parent root = loader.load();
        this.controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    public void setupTeams() {
        try {
            Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-Table.json"));
            this.controller.setTable(tablePersistence.readTable(reader));
        } catch (IOException e) {
            fail("Could not load json test-file.");
        }
        table = controller.getTable();
        assertNotNull(table);
        assertFalse(table.getTeams().isEmpty());
    }

    @Test
    public void testInitialTable() {
        assertNotNull(table);
        assertTrue(table.getTeams().size() == 16);
        int totalPoints = 0;
        for (Team team : table.getTeams()) {
            totalPoints += team.getPoints();
        }
        assertEquals(76, totalPoints);
    }

    @Test
    public void testSaveTable() {
        try {
            tablePersistence.saveTable(table, "test-Table.json");
        } catch (IOException e) {
            System.err.println("could not save table");
        }
    }

    @Test
    public void testAddPoints() {
        clickOn("#home");
        interact(() -> {
            controller.home.getSelectionModel().select(0);
        });
        clickOn("#away");
        interact(() -> {
            controller.away.getSelectionModel().select(2);
        });
        clickOn("#pointsA").write("0");
        clickOn("#pointsH").write("3");
        clickOn("#saveButton");
        table = controller.getTable();
        for (Team team : table.getTeams()) {
            if (team.getName().equals("GLIMT")) {
                assertEquals(13, team.getPoints());
            }
            if (team.getName().equals("ROSENBORG")) {
                assertEquals(10, team.getPoints());
            }
        }
    }
}