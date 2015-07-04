package com.gishump.bounce;

import com.badlogic.gdx.input.GestureDetector;

public class Gesture extends GestureDetector.GestureAdapter {
    private final Camera camera;
    private boolean flingDirection; // false=left, true=right

    public Gesture(Camera cam) {
        camera = cam;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        camera.setVelocity(0);
        camera.move(-deltaX*.25f);
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        camera.setVelocity(-velocityX*.005f);
        return true;
    }
}
