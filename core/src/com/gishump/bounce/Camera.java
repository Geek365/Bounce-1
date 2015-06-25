package com.gishump.bounce;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

public class Camera {
    private float cameraPosition;
    private final OrthographicCamera camera;

    public Camera(int width, int height) {
        camera = new OrthographicCamera(width, height);
        camera.position.set(width * .5f, height * .5f, 0);
        camera.update();
    }

    public void showLevel() {

    }

    public void checkFinishedShowing() {

    }

    public Matrix4 getCombinedMatrix() {
        return camera.combined;
    }



}
