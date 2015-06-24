package com.gishump.bounce;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Endpoint implements GameObject {
    protected final Body endpointBody;
    protected final FixtureDef endpointFixtureDef;
    protected final Fixture endpointFixture;
    protected final int horizontalEnd;
    
    public Endpoint(int x, int y, World world) {
        BodyDef box = new BodyDef();
        box.type = BodyDef.BodyType.StaticBody;
        box.position.set(x, y);
        endpointBody = world.createBody(box);
        PolygonShape endpointShape = new PolygonShape();
        endpointShape.setAsBox(10,10);
        endpointFixtureDef = new FixtureDef();
        endpointFixtureDef.friction = 2f;
        endpointFixtureDef.shape = endpointShape;
        endpointFixtureDef.restitution = .9f;
        endpointFixture = endpointBody.createFixture(endpointFixtureDef);
        endpointFixture.setSensor(true);
        endpointFixture.setUserData(Level.ENDPOINT);
        horizontalEnd = x + (10);
    }

    @Override
    public void draw(){ }

    @Override
    public void update(){ }

    @Override
    public Body getBody(){ return this.endpointBody; }

    @Override
    public int getHorizontalEnd() { return horizontalEnd; }
}
