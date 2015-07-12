package com.gishump.bounce;

import com.badlogic.gdx.InputAdapter;

public class Input extends InputAdapter {
    private int startx, starty;
    private enum touchMode {SLINGSHOT, SCROLL, NONE};
    private touchMode mode;
    private final RequestHandler androidHandler;
    private final Level level;
    private final Camera camera;

    public Input(Level lvl, RequestHandler ah, Camera cam) {
        level = lvl;
        androidHandler = ah;
        camera = cam;
    }

     @Override
     public boolean touchDown (int x, int y, int pointer, int button) {
         if (pointer == 0) {
             camera.setVelocity(0);
             startx = x;
             starty = y;
             if (x < 130 && y < 130 && Bounce.ball!=null) {
                 Bounce.state = Bounce.status.LOST;
                mode = touchMode.NONE;
             }
             else if (x > Bounce.rawWidth - 130 && y < 130) {
                 androidHandler.showMenu();
                 mode = touchMode.NONE;
             }
             else if (Bounce.ball == null && level.getCurrentSlingshot().isTouching((x / 5)+camera.getPosition(), Bounce.height - y / 5)) {
                 Bounce.ball = new Ball(x/5+camera.getPosition(), (Bounce.height - y / 5), 20, Level.world);
                 mode = touchMode.SLINGSHOT;
             }
             else {
                 mode = touchMode.SCROLL;
                 return false;
             }
         }
         return true;
     }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        if (pointer == 0) {
            if (mode == touchMode.SLINGSHOT && Bounce.ball!=null) {
                double strength = Math.hypot(startx - x, starty - y) / 80;
                Bounce.ball.setVelocity((startx-x) * (float)strength, -(starty-y) * (float)strength);
                Bounce.ball.released = true;
                if (level.getCurrentSlingshot().isBallTouching((x/5)+camera.getPosition(), Bounce.height - y / 5,
                        Bounce.ball.getPosition().x, Bounce.ball.getPosition().y, Bounce.ball.getRadius())) {
                    Bounce.ball.enableGravity();
                }
            }
            else if (mode == touchMode.SCROLL) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        if (pointer == 0) {
            if (mode == touchMode.SLINGSHOT && Bounce.ball!=null) {
                Bounce.ball.setPosition(x/5+camera.getPosition(),Bounce.height-y/5);
            }
            else if (mode == touchMode.SCROLL) {
                return false;
            }
        }
        return true;
    }
}

