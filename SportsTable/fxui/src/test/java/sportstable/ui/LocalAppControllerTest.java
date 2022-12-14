package sportstable.ui;

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
import sportstable.core.Table;
import sportstable.core.Team;
import sportstable.json.TablePersistence;
import sportstable.ui.LocalAppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;

/**
 * Test class for LocalAppcController
 */

public class LocalAppControllerTest extends ApplicationTest {

    private TablePersistence tablePersistence = new TablePersistence();
    private LocalAppController controller;
    private Table table;

    /**
     * Sets scene for the local app
     */

    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("LocalApp_test.fxml"));
        final Parent root = loader.load();
        this.controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Sets up the table before each test
     */

    @BeforeEach
    public void setupTable() {

        // Reading test table from json-file in resource folder.
        try {
            Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-Table.json"));
            this.controller.setTable(tablePersistence.readTable(reader));
        } catch (IOException e) {
            fail("Could not load json test-file.");
        }

        table = this.controller.getTable();
        controller.saveTable();
        controller.updateView();
        assertNotNull(table);
        assertFalse(table.getTeams().isEmpty());
    }

    /**
     * Tests that the total points from the teams in the initial table is as expected,
     * and that the table contains expected number of teams
     */

    @Test
    public void testInitialTable() {
        // Testing that the table has expected numbers of teams and points.
        assertNotNull(table);
        assertTrue(table.getTeams().size() == 16);
        int totalPoints = 0;
        for (Team team : table.getTeams()) {
            totalPoints += team.getPoints();
        }
        assertEquals(76, totalPoints);
    }

    /**
     * Tests that the save button works,
     * by adding points and team names. 
     * Checking if error messages are as expected, 
     * and that table values are as expected
     */

    @Test
    public void testHandleSave() {

        // Adding 3 points to first team in table (GLIMT)
        clickOn("#home");
        interact(() -> {
            this.controller.home.getSelectionModel().select(0);
        });
        clickOn("#away");
        interact(() -> {
            this.controller.away.getSelectionModel().select(2);
        });
        clickOn("#pointsA").write("0");
        clickOn("#pointsH").write("3");
        clickOn("#saveButton");

        // Checking if the points have been added in GLIMT-team
        table = this.controller.getTable();
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
            this.controller.home.getSelectionModel().select(0);
        });
        clickOn("#away");
        interact(() -> {
            this.controller.away.getSelectionModel().select(0);
        });
        clickOn("#pointsH").write("1");
        clickOn("#pointsA").write("1");
        clickOn("#saveButton");
        assertEquals("Invalid teams", this.controller.message.getText());
        for (Team team : table.getTeams()) {
            if (team.getName().equals("GLIMT")) {
                assertEquals(13, team.getPoints());
            }
        }

        // Trying to write invalid points to teams
        // Should catch exception and write message to user.
        clickOn("#home");
        interact(() -> {
            this.controller.home.getSelectionModel().select(0);
        });
        clickOn("#away");
        interact(() -> {
            this.controller.away.getSelectionModel().select(2);
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

    /**
     * Tests that the app saves the table, and that the table content are saved when closing and opening file.
     */

    @Test
    public void testSavingAndReadingTable() {

        // Saving testTable with one team to file in homefolder.
        Table testTable = new Table(new Team("TEST", 100));
        controller.setTable(testTable);
        controller.saveTable();

        // Reading the testTable from file in homefolder and
        // testing if it contain the test team with correct properties.
        Table savedTestTable = controller.getSavedTable();
        assertTrue(savedTestTable.iterator().hasNext());
        Team testTeam = savedTestTable.iterator().next();
        assertEquals("TEST", testTeam.getName());
        assertEquals(100, testTeam.getPoints());

        // Setting the controller table to the original test table from json-file again.
        controller.setTable(table);
        controller.saveTable();

        // Checking if the saved table in controller is correct, testing the first team
        // in list.
        Table originalTable = controller.getTable();
        assertTrue(originalTable.iterator().hasNext());
        Team originalFirstTeam = originalTable.iterator().next();
        assertEquals("GLIMT", originalFirstTeam.getName());
        assertEquals(10, originalFirstTeam.getPoints());
    }

    /**
     * Test that adding a team to a table works
     */

    @Test
    public void testAddTeam() {
        // Using edit window to add TestTeam
        // and checking if the size of the Team-list have increased with 1.
        assertTrue(controller.getTable().getTeams().size() == 16);
        clickOn("#editButton");
        assertNotNull(controller.getEditTableController());
        clickOn("#editTeamName").write("TestTeam");
        clickOn("#editTeamPoints").write("100");
        clickOn("#addTeamButton");
        clickOn("#saveTableButton");
        assertTrue(controller.getTable().getTeams().size() == 17);

        // Checking if the total points are increased with TestTeam points value (100);
        int totalPoints = 0;
        for (Team team : controller.getTable().getTeams()) {
            totalPoints += team.getPoints();
        }
        assertEquals(176, totalPoints);
    }

    /**
     * Tests that deleting a team from a table works
     */

    @Test
    public void testDeleteTeam() {

        // Using edit window to delete GLIMT team
        // and checking if the Team-list size have decreased with 1.
        assertTrue(controller.getTable().getTeams().size() == 16);
        clickOn("#editButton");
        clickOn("#selectedTeam").write("GLIMT");
        clickOn("#deleteTeamButton");
        clickOn("#saveTableButton");
        assertTrue(controller.getTable().getTeams().size() == 15);
    }

    /**
     * Tests that it works to open a new file. 
     * If file already exist in user.home folder, 
     * the test is opening the existing file
     */

    @Test
    public void testNewFile() {

        // Opening or creating testTableFile
        controller.fileNameInput.setText("");
        clickOn("#fileNameInput").write("testTableFile");
        clickOn("#openFileButton");

        // Making sure the table in file is empty
        controller.setTable(new Table());
        controller.saveTable();
        controller.updateView();
        assertTrue(controller.getTable().getTeams().size() == 0);

        // Adding new team and testing if size of table is now 1 and if the name is
        // correct.
        clickOn("#editButton");
        clickOn("#editTeamName").write("TestTeam");
        clickOn("#editTeamPoints").write("10");
        clickOn("#addTeamButton");
        clickOn("#saveTableButton");
        assertTrue(controller.getTable().getTeams().size() == 1);
        assertTrue(controller.getTable().getTeams().iterator().next().getName().equals("TestTeam"));

        // Removing team and making sure the table is now empty.
        clickOn("#editButton");
        clickOn("#selectedTeam").write("TestTeam");
        clickOn("#deleteTeamButton");
        clickOn("#saveTableButton");
        assertTrue(controller.getTable().getTeams().size() == 0);
    }

    /**
     * After each test, the table is set to intial values
     */

    @AfterEach
    public void cleanUp() {
        // Reading the initial test-Table json file and adding values to controller
        // Table.
        // Saving initial values in local test-Table file using saveTable method in
        // controller.
        try {
            Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-Table.json"));
            this.controller.setTable(tablePersistence.readTable(reader));
        } catch (IOException e) {
            fail("Could not load json test-file.");
        }
        table = this.controller.getTable();
        this.controller.saveTable();
    }
}