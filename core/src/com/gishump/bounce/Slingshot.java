package com.gishump.bounce;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Slingshot {
    final float xPosition;
    final float yPosition;
    final float width = 20;
    final float height = 30;
    final private Body slingshotBody;
    final private FixtureDef slingshotFixtureDef;
    final private Fixture slingshotFixture;
    public Slingshot(float x, float y, World world){
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
    }

    public boolean isTouching(float ballX, float ballY) {
        return (ballX >= xPosition-width && ballX <= xPosition+width && ballY >= yPosition-height && ballY <= yPosition+height);
    }

    public int getHorizontalEnd() {
        return (int)(xPosition + (width/2));
    }

    public void draw(){

    }

    public Body getBody() { return slingshotBody; }
}
