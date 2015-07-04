package com.gishump.bounce;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Slingshot {
    final int xPosition;
    final int yPosition;
    final float width = 20;
    final float height = 30;
    final private int ID;
    final private Body slingshotBody;
    final private FixtureDef slingshotFixtureDef;
    final private Fixture slingshotFixture;
    public Slingshot(int x, int y, int id, World world){
        BodyDef slingshot = new BodyDef();
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width/2, height/2);
        slingshot.type = BodyDef.BodyType.StaticBody;
        slingshot.position.set(x, y);
        slingshotBody = world.createBody(slingshot);
        slingshotFixtureDef = new FixtureDef();
        slingshotFixtureDef.shape = boxShape;
        slingshotFixture = slingshotBody.createFixture(slingshotFixtureDef);
        slingshotFixture.setSensor(true);
        slingshotFixture.setUserData(this);
        xPosition = x;
        yPosition = y;
        ID = id;
    }

    public boolean isTouching(float touchX, float touchY) {
        return (touchX >= xPosition-width && touchX <= xPosition+width && touchY >= yPosition-height && touchY <= yPosition+height);
    }

    public boolean isBallTouching() {
        Array<Contact> list = Level.world.getContactList();
        for (int i=0; i < list.size; i++) {
            Fixture a = list.get(i).getFixtureA();
            Fixture b = list.get(i).getFixtureB();
            if (list.get(i).isTouching() && ((a.getUserData()==Ball.class) || b.getUserData()==Ball.class) && (a.getUserData()==
                    Slingshot.class || b.getUserData()==Slingshot.class)) {
                return true;
            }
        }
        return false;
    }

    public int getID() { return ID; }

    public int getHorizontalEnd() {
        return (int)(xPosition + (width/2));
    }

    public void draw(){ }

    public Body getBody() { return slingshotBody; }
}
