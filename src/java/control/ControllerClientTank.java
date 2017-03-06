package control;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import model.Fish;
import org.bson.Document;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

/**
 * Created by MyPC on 3/1/2017.
 */
public class ControllerClientTank implements Initializable {

    @FXML
    private Pane pnRoot;

    private Socket mSocket;
    private Map<String, Fish> fishById;
    private Rectangle2D screen;
    private ObjectMapper mapper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        screen = Screen.getPrimary().getVisualBounds();
        Image image = new Image("background.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        pnRoot.setBackground(new Background(backgroundImage));
        fishById = new HashMap<>();
        mapper = new ObjectMapper();

        try {
            mSocket = new Socket("localhost", 2509);
            PrintStream printStream = new PrintStream(mSocket.getOutputStream());
            setupConnection(screen, printStream);

            Executors.newSingleThreadExecutor().execute(new FishHandler());
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

    class FishHandler implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

                while (true) {
                    String line = reader.readLine();
                    System.out.println(line);
                    List<Document> fishDocuments =
                            mapper.readValue(line, new TypeReference<List<Document>>() {
                            });

                    if (fishDocuments == null || fishDocuments.isEmpty()) {
                        continue;
                    }

                    for (Document fishDocument : fishDocuments) {

                        String id = fishDocument.getString("id");
                        double x = fishDocument.getDouble("x");
                        double y = fishDocument.getDouble("y");
                        String source = new File(fishDocument.getString("source")).getName();

                        Double rotation = fishDocument.getDouble("rotation");

                        //draw
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

                    Thread.sleep(10L);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
