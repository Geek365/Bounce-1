package com.gishump.bounce;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ball implements GameObject{
    private final Body ball;
    private final FixtureDef ballFixtureDef;
    private final Fixture ballFixture;
    public boolean released;

    public Ball (int x, int y, float rad, World world) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x,y);
        bdef.fixedRotation = false;
        ball = world.createBody(bdef);
        CircleShape cShape = new CircleShape();
        cShape.setRadius(rad);
        ballFixtureDef = new FixtureDef();
        ballFixtureDef.friction = .5f;
        ballFixtureDef.shape = cShape;
        ballFixtureDef.restitution = .9f;
        ballFixtureDef.density = .6f;
        ballFixture = ball.createFixture(ballFixtureDef);
        ballFixture.setUserData(this);
        ball.setGravityScale(0);
    }

    public void setPosition(Vector2 pos) { ball.setTransform(pos, ball.getAngle()); }

    public void setVelocity(float x, float y) { ball.setLinearVelocity(x, y); }

    public void enableGravity() { ball.setGravityScale(1f); }

    public int getHorizontalEnd() { return 0; }

    public void setPosition(float x, float y) { ball.setTransform(x,y,ball.getAngle()); }

    public Fixture getFixture() { return ballFixture; }

    @Override
    public void draw(){ }

    @Override
    public void update(){ }

    @Override
    public Body getBody(){ return this.ball; }
}
