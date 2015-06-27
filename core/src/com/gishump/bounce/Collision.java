package com.gishump.bounce;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Collision implements ContactListener {
    private Portal ptl; // Portal Reference
    private Ball bll; // Ball Reference
    private boolean collisionToProcess = false;
    private Fixture fixtureA, fixtureB;

    @Override
    public void beginContact(Contact contact) {
        fixtureA = contact.getFixtureA();
        fixtureB = contact.getFixtureB();
        if (fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
            collisionToProcess = true;
        }
        else {
            fixtureA = null;
            fixtureB = null;
            collisionToProcess = false;
        }
    }

    @Override
    public void endContact (Contact contact){ }

    @Override
    public void preSolve (Contact contact, Manifold oldManifold){ }

    @Override
    public void postSolve (Contact contact, ContactImpulse impulse){ }

    public void collisionHandler() {
        if (collisionToProcess) {
            if ((fixtureA.getUserData().getClass() == Portal.class) || (fixtureB.getUserData().getClass() == Portal.class)) {
                portalHandler();
            }
            else if ((fixtureA.getUserData().equals(Level.WALL)) || (fixtureB.getUserData().equals(Level.WALL))) {
                wallHandler();
            }
            else if ((fixtureA.getUserData().equals(Level.TRAMPOLINE)) || (fixtureB.getUserData().equals(Level.TRAMPOLINE))) {
                trampolineHandler();
            }
            else if ((fixtureA.getUserData().equals(Level.ENDPOINT)) || (fixtureB.getUserData().equals(Level.ENDPOINT))) {
                endpointHandler();
            }
            else if ((fixtureA.getUserData().getClass() == Slingshot.class) || (fixtureB.getUserData().getClass() == Slingshot.class)) {
                slingshotHandler();
            }
            collisionToProcess = false;
        }
    }

    public void portalHandler() {
        if (fixtureA.getUserData().getClass() == Portal.class) {
            ptl = (Portal) fixtureA.getUserData();
            bll = (Ball) fixtureB.getUserData();
            ptl.teleportBall(fixtureA, bll);
        }
        else {
            ptl = (Portal) fixtureB.getUserData();
            bll = (Ball) fixtureA.getUserData();
            ptl.teleportBall(fixtureB, bll);
        }
    }

    public void wallHandler() { Bounce.state = Bounce.status.LOST; }

    public void endpointHandler() {
        Bounce.state = Bounce.status.WON;
    }

    public void trampolineHandler() {
        if (fixtureA.getUserData().getClass() == Ball.class) {
            bll = (Ball) fixtureA.getUserData();
        }
        else {
            bll = (Ball) fixtureB.getUserData();
        }
        Vector2 ballVelocity = bll.getBody().getLinearVelocity();
        bll.setVelocity(ballVelocity.x*10, ballVelocity.y *10);
    }

    public void slingshotHandler() {
        // TODO Implement Slingshot Handler
    }

}
