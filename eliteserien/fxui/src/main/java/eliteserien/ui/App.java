package eliteserien.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Standalone version of the app
*/

public class App extends Application {

  /**
   * Loads the fxml file for the app and creates window/stage for it
  */

  @Override
  public void start(Stage stage) throws Exception {
    Parent parent = FXMLLoader.load(getClass().getResource("Table.fxml"));
    stage.setScene(new Scene(parent));
    stage.setResizable(false);
    stage.show();
  }

  /**
   * Launches the App
  */
  public static void main(String[] args) {
    launch(App.class, args);
  }
}
