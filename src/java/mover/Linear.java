package mover;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import utils.Constants;

import java.util.Random;

/**
 * Created by hiepdv on 11/12/2016.
 */
public class Linear implements Mover {

    private double maxWidth;
    private double maxHeight;
    private double yAdd = 1;
    private double xAdd = 1;
    private Random random;

    public Linear() {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        //Change to 800x600
//        this.maxHeight = Constants.HEIGHT_SCREEN;
//        this.maxWidth =Constants.WIDTH_SCREEN;


        this.maxHeight = screen.getHeight();
        this.maxWidth = screen.getWidth();
        random = new Random();
    }

    @Override
    public void move(ImageView imageFish) {
        Bounds imageBounds = imageFish.localToScreen(imageFish.getBoundsInLocal());

        if (imageBounds.getMaxY() > maxHeight || imageBounds.getMinY() < 0) {
            yAdd = -yAdd;
        }

        if (imageBounds.getMaxX() > maxWidth || imageBounds.getMinX() < 0) {
            xAdd = -xAdd;
        }

        if (xAdd < 0) {
            imageFish.setRotate(0);
        }

        if (xAdd > 0) {
            imageFish.setRotate(180);
        }

        double yNext = imageFish.getY() + yAdd * (1 + random.nextInt(1));
        double xNext = imageFish.getX() + xAdd * (1 + random.nextInt(2));

        imageFish.setX(xNext);
        imageFish.setY(yNext);
    }
}
