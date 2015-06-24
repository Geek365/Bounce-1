package com.gishump.bounce;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface GameObject {
    void update();
    void draw();
    Body getBody();
    int getHorizontalEnd();
}
