package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import mover.Mover;

import java.io.Serializable;

/**
 * Created by hiepdv on 11/12/2016.
 */
public class Fish extends AnimationTimer implements Serializable{

    private String id;
    private int deviceId;
    private ImageView imageFish;
    private Mover mover;
    @JsonIgnore
    public long    personId = 0;
    private static final long serialVersionUID = 1L;
    public Fish(ImageView imageFish, Mover mover) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.imageFish = imageFish;
        this.mover = mover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public ImageView getImageFish() {
        return imageFish;
    }

    public void setImageFish(ImageView imageFish) {
        this.imageFish = imageFish;
    }

    public Mover getMover() {
        return mover;
    }

    public void setMover(Mover mover) {
        this.mover = mover;
    }

    @Override
    public void handle(long l) {
        mover.move(imageFish);

    }
}
