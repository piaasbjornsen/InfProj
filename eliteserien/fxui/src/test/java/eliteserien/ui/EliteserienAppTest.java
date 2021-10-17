package eliteserien.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eliteserien.json.TablePersistence;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class EliteserienAppTest extends ApplicationTest {

  private EliteserienAppController controller;
  private TablePersistence tablePersistence = new TablePersistence();

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("Table_test.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  public void setupTable() {
    try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-Table.json"))) {
      this.controller.setTable(tablePersistence.readTable(reader));
    } catch (IOException e) {
      fail("Could not load json test-file.");
    }
  }

  @Test
  public void testController_initial() {
    assertNotNull(this.controller);
  }
}
