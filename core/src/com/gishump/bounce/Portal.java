package com.gishump.bounce;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Portal{
    protected int aX, aY, bX, bY;
    protected final Body portalA;
    protected final Body portalB;
    private final FixtureDef portalAFixtureDef;
    private final FixtureDef portalBFixtureDef;
    private final Fixture portalAFixture;
    private final Fixture portalBFixture;
    private boolean locked = false;

    public Portal (int ax, int ay, int bx, int by, World world) {
        BodyDef portal = new BodyDef();
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(10,10);
        // Create Portal A
        portal.type = BodyDef.BodyType.StaticBody;
        portal.position.set(ax, ay);
        portalA = world.createBody(portal);
        portalAFixtureDef = new FixtureDef();
        portalAFixtureDef.shape = boxShape;
        portalAFixture = portalA.createFixture(portalAFixtureDef);
        portalAFixture.setSensor(true);
        portalAFixture.setUserData(this);
        // Create Portal B
        portal.position.set(bx, by);
        portalB = world.createBody(portal);
        portalBFixtureDef = new FixtureDef();
        portalBFixtureDef.shape = boxShape;
        portalBFixture = portalB.createFixture(portalAFixtureDef);
        portalBFixture.setSensor(true);
        portalBFixture.setUserData(this);
        aX = ax;
        aY = ay;
        bX = bx;
        bY = by;
    }

    public void teleportBall(Fixture entryPoint, Ball ball) {
        if (!locked) {
            if (entryPoint == portalAFixture) {
                ball.setPosition(portalB.getPosition());
            }
            else if (entryPoint == portalBFixture) {
                ball.setPosition(portalA.getPosition());
            }
            locked = true;
        }
        else { locked = false; }
    }

    public void draw() { }

    public void update() { }

    public int getHorizontalEnd() { return ((aX > bX) ? aX:bX) + 10;}

    public Body getPortalABody() { return portalA; }

    public Body getPortalBBody() { return portalB; }
}