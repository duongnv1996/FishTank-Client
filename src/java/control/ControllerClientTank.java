package control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import model.Fish;
import org.bson.Document;
import sun.security.util.Debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by MyPC on 3/1/2017.
 */
public class ControllerClientTank implements Initializable {

    @FXML
    private Pane pnRoot;

    private Socket mSocket;
    private Map<String, Fish> fishById;
    private Rectangle2D screen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        screen = Screen.getPrimary().getVisualBounds();
        Image image = new Image("background.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        pnRoot.setBackground(new Background(backgroundImage));
        fishById = new HashMap<>();


        try {
            mSocket = new Socket("localhost", 2509);
            PrintStream printStream = new PrintStream(mSocket.getOutputStream());
            setupConnection(screen, printStream);
            new FishHandler().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupConnection(Rectangle2D screen, PrintStream printStream) {
        Document message = new Document();
        message.put("width", (int) screen.getWidth());
        message.put("height", (int) screen.getHeight());
        printStream.println(message.toJson());
    }

    class FishHandler extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

                while (true) {
                    String msg = reader.readLine();
                    if (msg != null) {
                        Document document = Document.parse(msg);
                        String id = document.getString("id");
                        double x = document.getDouble("x");
                        double y = document.getDouble("y");
                        String source = document.getString("source");
                        Integer rotation = document.getInteger("rotation");

                        //draw
                        Debug.println("MSG " + screen.getWidth(), msg);
                        Fish fish = fishById.get(id);

                        if (fish == null) {
                            Fish newFish = new Fish(source);
                            newFish.move(x, y, rotation);

                            fishById.put(id, newFish);
                            Platform.runLater(() -> pnRoot.getChildren().add(newFish.getImageFish()));
                            continue;
                        }

                        Platform.runLater(() -> {
                            fish.move(x, y, rotation);
                        });

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
