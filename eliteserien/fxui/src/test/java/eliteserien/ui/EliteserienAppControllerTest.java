package eliteserien.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.jupiter.api.AfterEach;
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
        controller.saveTable();
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
    public void testHandleSave() {
        // adding 3 points to first team in table (GLIMT)
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

        // checking if the points have been added in GLIMT-team
        table = controller.getTable();
        for (Team team : table.getTeams()) {
            if (team.getName().equals("GLIMT")) {
                assertEquals(13, team.getPoints());
            }
            if (team.getName().equals("ROSENBORG")) {
                assertEquals(10, team.getPoints());
            }
        }

        // Adding same team for home and away.
        // Should catch exception and write message to user.
        clickOn("#home");
        interact(() -> {
            controller.home.getSelectionModel().select(0);
        });
        clickOn("#away");
        interact(() -> {
            controller.away.getSelectionModel().select(0);
        });
        clickOn("#pointsH").write("1");
        clickOn("#saveButton");
        assertEquals("Choose two different teams", controller.message.getText());
        for (Team team : table.getTeams()) {
            if (team.getName().equals("GLIMT")) {
                assertEquals(13, team.getPoints());
            }
        }

        // Trying to write invalid points to teams
        // Should catch exception and write message to user.
        clickOn("#home");
        interact(() -> {
            controller.home.getSelectionModel().select(0);
        });
        clickOn("#away");
        interact(() -> {
            controller.away.getSelectionModel().select(2);
        });
        clickOn("#pointsA").write("-3");
        clickOn("#saveButton");
        assertEquals("Invalid points", controller.message.getText());
        for (Team team : table.getTeams()) {
            if (team.getName().equals("ROSENBORG")) {
                assertEquals(10, team.getPoints());
            }
        }
    }

    @Test
    public void testSavingAndReadingTable() {
        // saving testTable with one team to file in homefolder. 
        Table testTable = new Table(new Team("TEST", 100));
        controller.setTable(testTable);
        controller.saveTable();

        // reading the testTable from file in homefolder and 
        // testing if it contain the test team with correct properties.
        Table savedTestTable = controller.getSavedTable();
        assertTrue(savedTestTable.iterator().hasNext());
        Team testTeam = savedTestTable.iterator().next();
        assertEquals("TEST", testTeam.getName());
        assertEquals(100, testTeam.getPoints());

        // setting the controller table to the original test table from json-file again. 
        controller.setTable(table); 
        controller.saveTable();

        //checking if the saved table in controller is correct, testing the first team in list.
        Table originalTable = controller.getTable();
        assertTrue(originalTable.iterator().hasNext());
        Team originalFirstTeam = originalTable.iterator().next();
        assertEquals("GLIMT", originalFirstTeam.getName());
        assertEquals(10, originalFirstTeam.getPoints());
    }

    @AfterEach
    public void cleanUp() {
        try {
            Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-Table.json"));
            this.controller.setTable(tablePersistence.readTable(reader));
        } catch (IOException e) {
            fail("Could not load json test-file.");
        }
        table = controller.getTable();
        controller.saveTable();
    }
}