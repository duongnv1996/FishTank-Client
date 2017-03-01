package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.Constants;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/layout_client_tank.fxml"));
        primaryStage.setTitle("Fish tank client");
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        primaryStage.setScene(new Scene(root, Constants.WIDTH_SCREEN, screen.getHeight()));
        primaryStage.show();
        primaryStage.setFullScreen(true);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
