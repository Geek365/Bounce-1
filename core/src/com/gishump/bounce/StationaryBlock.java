package com.gishump.bounce;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class StationaryBlock implements GameObject {
    protected final Body boxBody;
    protected final FixtureDef boxFixtureDef;
    protected final Fixture boxFixture;
    protected int horizontalEnd;

    public StationaryBlock(int x, int y, int width, int height, float angle, World world) {
        BodyDef box = new BodyDef();
        box.type = BodyDef.BodyType.KinematicBody;
        box.position.set(x, y);
        boxBody = world.createBody(box);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width/2,height/2);
        boxFixtureDef = new FixtureDef();
        boxFixtureDef.friction = 2f;
        boxFixtureDef.shape = boxShape;
        boxFixtureDef.restitution = .9f;
        boxFixture = boxBody.createFixture(boxFixtureDef);
        boxFixture.setUserData(this);
        horizontalEnd = x + (width/2);
    }

    @Override
    public void draw(){ }

    @Override
    public void update(){ }

    @Override
    public Body getBody(){ return this.boxBody; }

    @Override
    public int getHorizontalEnd() { return horizontalEnd; }
}
