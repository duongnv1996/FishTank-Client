package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import socket.SocketClient;
import utils.Constants;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Fish tank");
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        primaryStage.setScene(new Scene(root, Constants.WIDTH_SCREEN, screen.getHeight()));
        primaryStage.show();
        primaryStage.setFullScreen(true);
        SocketClient client = new SocketClient();
        client.connectSocket();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
