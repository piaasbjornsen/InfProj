package eliteserien.ui;

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
 * integration test
 * Checks if the server connects to the localhost uri and can access the table. 
 * The server at port 8999 needs to be running for this test to work.
 */


public class TableAppIT extends ApplicationTest {

  @BeforeAll
  public static void setupHeadless() {
    RemoteApp.supportHeadless();
  }

  private RemoteAppController controller;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("TableApp_it.fxml"));
    final Parent root = loader.load();
    this.controller = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  public void setupTable() throws URISyntaxException {
    try {
      String port = "8999";
      URI baseUri = new URI("http://localhost:" + port + "/eliteserien/");
      this.controller.setTable(this.controller.getUriTable(baseUri));
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testController_initial() {
    assertNotNull(this.controller);
  }

  @Test
  public void testURITable() {
      assertTrue(this.controller.getTable().getTeams().iterator().hasNext());
      assertEquals(this.controller.getTable().getTeams().iterator().next().getName(), "test2");
  }
}
