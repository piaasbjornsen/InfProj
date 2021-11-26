package sportstable.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * Integration test
 * Checks if the server connects to the localhost uri and can access the table. 
 * The server at port 8999 needs to be running for this test to work.
 */


public class TableAppIT extends ApplicationTest {

  /**
   * Sets up the helper method supportHeadless used by tests needing to run headless,
   * before all tests can start.
  */

  @BeforeAll
  public static void setupHeadless() {
    RemoteApp.supportHeadless();
  }

  private RemoteAppController controller; // An instance of a RemoteAppController

  /**
   * Sets scene from TableApp_it.fxml file
   */

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("TableApp_it.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Sets up a table at the uri before each test
   */

  @BeforeEach
  public void setupTable() throws URISyntaxException {
    try {
      String port = "8999";
      URI baseUri = new URI("http://localhost:" + port + "/sportstable/");
      this.controller.setTable(this.controller.getUriTable(baseUri));
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  /**
   * Tests that the controller is set
   */

  @Test
  public void testController_initial() {
    assertNotNull(this.controller);
  }

  /**
   * Tests that the table content in URI is the same as the content from the config json file
   */

  @Test
  public void testURITable() {
      assertTrue(this.controller.getTable().getTeams().iterator().hasNext());
      assertEquals(this.controller.getTable().getTeams().iterator().next().getName(), "test2");
  }
}
