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
        cameraPosition = Bounce.width - Level.currentLevelWidth;
        if (cameraPosition<Bounce.width/4) { cameraPosition = 0; } // Don't Bother Showing Level
        else { Bounce.state = Bounce.status.IDLE; }
    }

    public void checkFinishedShowing() {
        camera.position.set((Bounce.width * .5f)+ cameraPosition, Bounce.height * .5f, 0);
        if (cameraPosition == 0) Bounce.state = Bounce.status.RUNNING;
        else cameraPosition--;
    }

    public Matrix4 getCombinedMatrix() {
        return camera.combined;
    }

}
