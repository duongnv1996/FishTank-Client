package control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
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
    Pane pnRoot;

    private Socket mSocket;
    private Map<String, ImageView> mMapFish;
    Rectangle2D screen;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         screen= Screen.getPrimary().getVisualBounds();
        Image image = new Image("background.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        pnRoot.setBackground(new Background(backgroundImage));
        mMapFish = new HashMap<>();


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
        Document document = new Document();
        document.put("width", (int) screen.getWidth());
        document.put("height", (int) screen.getHeight());
        printStream.println(document.toJson());
    }

    class FishHandler extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                while (true) {
                    String msg = br.readLine();
                    if (msg != null) {
                        Document document = Document.parse(msg);
                        final String id = document.getString("id");
                        final double x = document.getDouble("x");
                        final double y = document.getDouble("y");
                        String source = document.getString("source");

                        //draw
                        if (x >= screen.getWidth()) {
                            Debug.println("MSG "+ screen.getWidth(), msg);
                            if (!mMapFish.containsKey(id)) {
                                final ImageView imageView = new ImageView(source);
                                imageView.setX(x-screen.getWidth());
                                imageView.setY(y);
                                mMapFish.put(id, imageView);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        pnRoot.getChildren().add(imageView);

                                    }
                                });
                            } else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        ImageView imageView = mMapFish.get(id);
                                      imageView.setY(y);
                                      Debug.println("x", String.valueOf(x-screen.getWidth()));
                                      imageView.setX(x - screen.getWidth());

                                    }
                                });


                            }

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
