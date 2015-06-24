package com.gishump.bounce;

import com.badlogic.gdx.physics.box2d.World;

public class HorizontalSlidingBlock extends StationaryBlock {
    private boolean direction = false; // false = right, true = left
    private final int start;
    private final int end;
    private final float yposition;
    private float xposition;
    private final float speed;

    public HorizontalSlidingBlock(int x, int y, int endpoint, int width, int height, float spd, World world) {
        super(x, y, width, height, 0f, world);
        start = (x > endpoint) ? endpoint:x;
        end = (x > endpoint) ? x:endpoint;
        yposition = y;
        xposition = x;
        direction = (start > endpoint);
        speed = spd;
        horizontalEnd = end + (width/2);
    }

    @Override
    public void draw() { }

    @Override
    public void update() {
            if (!direction) {
                if (xposition>=end) {direction = true;}
                else {xposition+=speed; boxBody.setTransform(xposition, yposition, 0);}
            }
            else {
                if (xposition<=start) {direction = false;}
                else {xposition-=speed;boxBody.setTransform(xposition, yposition, 0);}
            }
    }
}
