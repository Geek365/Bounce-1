package com.gishump.bounce;

import com.badlogic.gdx.physics.box2d.World;

public class VerticalSlidingBlock extends StationaryBlock implements GameObject {
    private boolean direction = false; // false = up , true = down
    private final int start;
    private final int end;
    private float yposition;
    private final float xposition;
    private final float speed;

    public VerticalSlidingBlock(int x, int y, int endpoint, int width, int height, float spd, World world) {
        super(x, y, width, height, 0f, world);
        start = (y > endpoint) ? endpoint:y;
        end = (y > endpoint) ? y:endpoint;
        yposition = y;
        xposition = x;
        direction = (start > endpoint);
        speed = spd;
    }

    @Override
    public void draw() { }

    @Override
    public void update() {
        if (!direction) {
            if (yposition>=end) {direction = true;}
            else {yposition+=speed; boxBody.setTransform(xposition, yposition, 0);}
        }
        else {
            if (yposition<=start) {direction = false;}
            else {yposition-=speed; boxBody.setTransform(xposition, yposition, 0);}
        }
    }
}

