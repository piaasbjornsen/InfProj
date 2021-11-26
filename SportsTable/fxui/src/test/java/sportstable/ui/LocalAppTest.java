package sportstable.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sportstable.json.TablePersistence;
import sportstable.ui.LocalAppController;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Test class for LocalApp
 */

public class LocalAppTest extends ApplicationTest {

  private LocalAppController controller;
  private TablePersistence tablePersistence = new TablePersistence();

  /**
   * Sets scene for the application
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
    try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-Table.json"))) {
      this.controller.setTable(tablePersistence.readTable(reader));
    } catch (IOException e) {
      fail("Could not load json test-file.");
    }
  }

  /**
   * Tests that the contorller is set
   */

  @Test
  public void testController_initial() {
    assertNotNull(this.controller);
  }
}
