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
             startx = x;
             starty = y;
             if (x < 80 && y < 80) {
                 androidHandler.showMenu();
                 mode = touchMode.NONE;
             }
             else if (x/5 > Bounce.width - 80 && y/5 > Bounce.height - 80) {
                 Bounce.state = Bounce.status.LOST;
                 mode = touchMode.NONE;
             }
             else if (Bounce.ball == null && level.getCurrentSlingshot().isTouching((x / 5), Bounce.height - y / 5)) {
                 Bounce.ball = new Ball(x / 5, (Bounce.height - y / 5), 20, Level.world);
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
                if (level.getCurrentSlingshot().isBallTouching()) {
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
                Bounce.ball.setPosition(x/5,Bounce.height-y/5);
            }
            else if (mode == touchMode.SCROLL) {
                return false;
            }
        }
        return true;
    }
}

