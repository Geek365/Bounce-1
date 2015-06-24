package com.gishump.bounce;

import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor {
    private int startx, starty;
    private boolean lock;
    private RequestHandler androidHandler;
    private Level level;

    public Input(Level lvl, RequestHandler ah) {
        level = lvl;
        androidHandler = ah;
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
             if (x < 80 && y < 80) {
                 androidHandler.showMenu();
             }
             else {
                 startx = x;
                 starty = y;
                 if (Bounce.ball == null && level.getCurrentSlingshot().isTouching(x / 5, Bounce.height - y / 5)) {
                     Bounce.ball = new Ball(x / 5, (Bounce.height - y / 5), 20, Level.world);
                     lock = false;
                 } else {
                     lock = true;
                 }
             }
         }
         return false;
     }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        if (pointer == 0 && Bounce.ball!=null) {
            double strength = Math.hypot(startx - x, starty - y) / 80;
            if (!lock) {
                Bounce.ball.setVelocity((startx-x) * (float)strength, -(starty-y) * (float)strength);
                Bounce.ball.enableGravity();
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        if (pointer == 0 && Bounce.ball!=null) {
            if (!lock) {
                Bounce.ball.setPosition(x/5,Bounce.height-y/5);
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

