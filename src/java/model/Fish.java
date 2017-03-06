package model;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by hiepdv on 11/12/2016.
 */
public class Fish {

    private String id;
    private ImageView imageFish;

    public Fish(String source) {
        this.id = String.valueOf(System.currentTimeMillis());
        Image image = new Image(source);

        this.imageFish = new ImageView(image);
        this.imageFish.setRotationAxis(new Point3D(0, 1, 0));

    }

    public void move(Double x, Double y, Double rotation) {
        this.imageFish.setX(x);
        this.imageFish.setY(y);
        this.imageFish.setRotate(rotation);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ImageView getImageFish() {
        return imageFish;
    }

    public void setImageFish(ImageView imageFish) {
        this.imageFish = imageFish;
    }
}
