package eliteserien.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RemoteApp extends Application {
    /**
   * Helper method used by tests needing to run headless.
   */
  
  public static void supportHeadless() {
    if (Boolean.getBoolean("headless")) {
      System.setProperty("testfx.robot", "glass");
      System.setProperty("testfx.headless", "true");
      System.setProperty("prism.order", "sw");
      System.setProperty("prism.text", "t2k");
      System.setProperty("java.awt.headless", "true");
    }
  }

    
    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("EliteserienRemoteApp.fxml"));
        stage.setScene(new Scene(parent));
        stage.show();
    }

    public static void main(String[] args) {
        launch(RemoteApp.class, args);
    }
}
