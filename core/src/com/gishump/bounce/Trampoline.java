package com.gishump.bounce;

import com.badlogic.gdx.physics.box2d.World;

public class Trampoline extends StationaryBlock {
    public Trampoline(int x, int y, int width, float angle, World world) {
        super(x, y, width, 5, angle, world);
        boxFixture.setUserData(Level.TRAMPOLINE);
    }
}
