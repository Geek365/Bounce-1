package com.gishump.bounce;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.TimeUtils;

public class Camera {
    private float position;
    private float velocity;
    private final float deacceleration = .09f;
    private final OrthographicCamera camera;

    public Camera(int width, int height) {
        camera = new OrthographicCamera(width, height);
        camera.position.set(width * .5f, height * .5f, 0);
        camera.update();
    }

    public void showLevel() {
        position = Level.currentLevelWidth - Bounce.width;
        if (position<Bounce.width/2) { // Don't Bother Showing Level
            position = 0;
            Bounce.state = Bounce.status.RUNNING;
        }
        else {
            Bounce.state = Bounce.status.IDLE;
        }
    }

    public void move(float delta) {
        if (delta + position < 0) {
            position = 0;
            velocity = 0;
        }
        else if (delta + position > (Level.currentLevelWidth - Bounce.width)) {
            position = Level.currentLevelWidth - Bounce.width;
            velocity = 0;
        }
        else {
            position += delta;
        }
        camera.position.set(Bounce.width * .5f + position, Bounce.height * .5f, 0);
    }

    public void setVelocity(float vel) { velocity = vel; }


    public float getPosition() { return position; }

    public void checkFinishedShowing() {
        camera.position.set((Bounce.width * .5f) + position, Bounce.height * .5f, 0);
        camera.update();
        if (position <= 0) { position=0 ; Bounce.state = Bounce.status.RUNNING; }
        else position-=2;
    }

    public void update() {
        if (velocity != 0) {
            System.out.println(velocity);
            move(velocity);
            //velocity *= deacceleration;
        }
        camera.update();
    }

    public Matrix4 getCombinedMatrix() {
        return camera.combined;
    }
}
