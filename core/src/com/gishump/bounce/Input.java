package com.gishump.bounce;

import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor {
    private int startx, starty;
    private enum touchMode {SLINGSHOT, SCROLL, NONE}; // false = slingshot, true = scrolling around level
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
    public boolean keyDown (int keycode) {
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped (char character) {
        return false;
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
             else if (Bounce.ball == null && level.getCurrentSlingshot().isTouching(x / 5, Bounce.height - y / 5)) {
                 Bounce.ball = new Ball(x / 5, (Bounce.height - y / 5), 20, Level.world);
                 mode = touchMode.SLINGSHOT;
             }
             else {
                 mode = touchMode.SCROLL;
             }
         }
         return false;
     }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        if (pointer == 0) {
            if (mode == touchMode.SLINGSHOT && Bounce.ball!=null) {
                double strength = Math.hypot(startx - x, starty - y) / 80;
                Bounce.ball.setVelocity((startx-x) * (float)strength, -(starty-y) * (float)strength);
                Bounce.ball.enableGravity();
            }
            else if (mode == touchMode.SCROLL) {
                // TODO Implement Kinetic Scrolling
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        if (pointer == 0) {
            if (mode == touchMode.SLINGSHOT && Bounce.ball!=null) {
                Bounce.ball.setPosition(x/5,Bounce.height-y/5);
            }
            else if (mode == touchMode.SCROLL) {
                // TODO Implement Scrolling
            }
        }
        return false;
    }

    @Override
    public boolean scrolled (int amount) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return true;
    }
}

