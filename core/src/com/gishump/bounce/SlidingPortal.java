package com.gishump.bounce;

import com.badlogic.gdx.physics.box2d.World;

public class SlidingPortal extends Portal{
    private int aStart, bStart;
    private int aEndpoint, bEndpoint;
    private float aSpeed, bSpeed;
    private boolean aMovement, bMovement; // false = vertical, true = horizontal
    private boolean aDirection, bDirection; // false = up/right , true = down/left

    public SlidingPortal(int aX, int aY, int bX, int bY, int aEnd, int bEnd,
                         boolean aMvmt, boolean bMvmt, float aSpd, float bSpd,  World world) {
        super (aX, aY, bX, bY, world);
        aStart = (aMvmt ? aX:aY)> aEnd ? aEnd:(aMvmt ? aX:aY);
        bStart = (bMvmt ? bX:bY)> bEnd ? bEnd:(bMvmt ? bX:bY);
        aEndpoint = (aMvmt ? aX:aY)< aEnd ? aEnd:(aMvmt ? aX:aY);
        bEndpoint = (bMvmt ? bX:bY)< bEnd ? bEnd:(bMvmt ? bX:bY);
        aSpeed = aSpd;
        bSpeed = bSpd;
        aMovement = aMvmt;
        bMovement = bMvmt;
    }

    @Override
    public void draw() { }

    @Override
    public void update() {
        // Move Portal A
        if (!aMovement) {
            if (aDirection) {
                if (aY>=aEndpoint) {aDirection = true;}
                else {aY+=aSpeed; portalA.setTransform(aX, aY, 0);}
            }
            else {
                if (aY<=aStart) {aDirection = false;}
                else {aY-=aSpeed; portalA.setTransform(aX, aY, 0);}
            }
        }
        else {
            if (!aDirection) {
                if (aX>=aEndpoint) {aDirection = true;}
                else {aX+=aSpeed; portalA.setTransform(aX, aY, 0);}
            }
            else {
                if (aX<=aStart) {aDirection = false;}
                else {aX-=aSpeed;portalA.setTransform(aX, aY, 0);}
            }
        }
        // Move Portal B
        if (!bMovement) {
            if (bDirection) {
                if (bY>=bEndpoint) {bDirection = true;}
                else {bY+=bSpeed; portalB.setTransform(bX, bY, 0);}
            }
            else {
                if (bY<=bStart) {bDirection = false;}
                else {bY-=bSpeed; portalB.setTransform(bX, bY, 0);}
            }
        }
        else {
            if (!bDirection) {
                if (bX>=bEndpoint) {bDirection = true;}
                else {bX+=bSpeed; portalB.setTransform(bX, bY, 0);}
            }
            else {
                if (bX<=bStart) {bDirection = false;}
                else {bX-=bSpeed;portalB.setTransform(bX, bY, 0);}
            }
        }
    }

    @Override
    public int getHorizontalEnd() {
        return (aEndpoint > bEndpoint ? aEndpoint:bEndpoint) + 10;
    }

}