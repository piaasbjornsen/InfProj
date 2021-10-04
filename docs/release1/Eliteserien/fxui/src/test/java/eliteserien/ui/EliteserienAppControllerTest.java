package eliteserien.ui;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;

import eliteserien.core.Table;
import eliteserien.json.TablePersistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EliteserienAppControllerTest extends ApplicationTest {

    private TablePersistence tablePersistence = new TablePersistence();
    private EliteserienAppController controller;


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
            this.controller.setTable(tablePersistence.readTable(new InputStreamReader(getClass().getResourceAsStream("test-Table.json"))));
        } catch (IOException e) {
            fail("Could not load json test-file.");
        }
        assertNotNull(this.controller);
        assertNotNull(this.controller.getTable());
    }
}