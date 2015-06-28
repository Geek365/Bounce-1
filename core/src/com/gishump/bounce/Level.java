package com.gishump.bounce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;

public class Level {
    // Object ID Declarations (For Collision Callbacks)
    public static final Byte WALL = Byte.valueOf((byte)20);
    public static final Byte TRAMPOLINE = Byte.valueOf((byte)5);
    public static final Byte ENDPOINT = Byte.valueOf((byte)2);
    // Field Declarations
    public static World world;
    private Body bottomWall, topWall, leftWall, rightWall;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<Portal> portals;
    private ArrayList<Slingshot> slingshots;
    public static int currentSlingshot;
    public static int currentLevelWidth;
    private Camera camera;

    public Level(Camera cam) {
        camera = cam;
        world = new World(new Vector2(0, -30), true);
        gameObjects = new ArrayList<GameObject>();
        portals = new ArrayList<Portal>();
        slingshots = new ArrayList<Slingshot>();
    }

    public void clearLevel() {
        for (GameObject g : gameObjects) { world.destroyBody(g.getBody()); }
        for (Portal p : portals) {
            world.destroyBody(p.getPortalABody());
            world.destroyBody(p.getPortalBBody());
        }
        for (Slingshot s : slingshots) { world.destroyBody(s.getBody()); }
        if (bottomWall!=null) world.destroyBody(bottomWall);
        if (topWall!= null) world.destroyBody(topWall);
        if (leftWall!= null) world.destroyBody(leftWall);
        if (rightWall!= null) world.destroyBody(rightWall);
        gameObjects.clear();
        portals.clear();
        slingshots.clear();
    }

    private boolean levelExists(int levelnum) {
        try {
            Gdx.files.internal("levels/" + levelnum + ".lvl");
            return true;
        }
        catch (GdxRuntimeException e) { return false; }
    }

    public void loadLevel(int levelnum){
        if (levelExists(levelnum)) {
            clearLevel();
            currentSlingshot = 0;
            String currentline[];
            int currentid;
            int slingshotcount = 0;
            FileHandle file = Gdx.files.internal("levels/" + levelnum + ".lvl");
            String text = file.readString();
            String[] lines = text.split("\n");
            for (String s : lines) {
                currentline = s.split(" ");
                currentid = Integer.parseInt(currentline[0]);
                if (currentid == 0) {
                    gameObjects.add(new VerticalSlidingBlock(Integer.parseInt(currentline[1]), Integer.parseInt(currentline[2]), Integer.parseInt(currentline[3]), Integer.parseInt(currentline[4]), Integer.parseInt(currentline[5]), Float.parseFloat(currentline[6]), world));
                } else if (currentid == 1) {
                    gameObjects.add(new HorizontalSlidingBlock(Integer.parseInt(currentline[2]), Integer.parseInt(currentline[1]), Integer.parseInt(currentline[3]), Integer.parseInt(currentline[4]), Integer.parseInt(currentline[5]), Float.parseFloat(currentline[6]), world));
                } else if (currentid == 2) {
                    gameObjects.add(new StationaryBlock(Integer.parseInt(currentline[1]), Integer.parseInt(currentline[2]), Integer.parseInt(currentline[3]), Integer.parseInt(currentline[4]), 0f, world));
                } else if (currentid == 3) {
                    portals.add(new Portal(Integer.parseInt(currentline[1]), Integer.parseInt(currentline[2]), Integer.parseInt(currentline[3]), Integer.parseInt(currentline[4]), world));
                } else if (currentid == 4) {
                    portals.add(new SlidingPortal(Integer.parseInt(currentline[1]), Integer.parseInt(currentline[2]), Integer.parseInt(currentline[3]), Integer.parseInt(currentline[4]), Integer.parseInt(currentline[5]), Integer.parseInt(currentline[6]), (currentline[7] == "horizontal"), (currentline[8] == "horizontal"), Float.parseFloat(currentline[9]), Float.parseFloat(currentline[10]), world));
                } else if (currentid == 5) {
                    gameObjects.add(new Trampoline(Integer.parseInt(currentline[1]), Integer.parseInt(currentline[2]), Integer.parseInt(currentline[3]), Float.parseFloat(currentline[4]), world));
                } else if (currentid == 6) {
                    slingshots.add(new Slingshot(Integer.parseInt(currentline[1]), Integer.parseInt(currentline[2]), slingshotcount++, world));
                } else if (currentid == 7) {
                    // TODO Implement Flame Block Class And Tie It In Here
                } else if (currentid == 8) {
                    gameObjects.add(new Endpoint(Integer.parseInt(currentline[1]),Integer.parseInt(currentline[2]), world));
                }
            }
            createWalls();
            slingshots.add(new Slingshot(300, 150, 0, world)); // For Testing
            gameObjects.add(new Trampoline(230, 23, 60, 0, world)); // For Testing
            gameObjects.add(new Endpoint(1, 100, world));
            camera.showLevel();
        }
    }

    public Slingshot getCurrentSlingshot() { return slingshots.get(currentSlingshot); }

    public int getLevelWidth() {
        int max=0;
        for (GameObject g : gameObjects) {
            if (max<g.getHorizontalEnd()) { max = g.getHorizontalEnd(); }
        }
        for (Portal p : portals) {
            if (max<p.getHorizontalEnd()) { max = p.getHorizontalEnd(); }
        }
        for (Slingshot s : slingshots) {
            if (max<s.getHorizontalEnd()) { max = s.getHorizontalEnd(); }
        }
        return (max>Bounce.width) ? max:Bounce.width;
    }

    public void createWalls() {
        currentLevelWidth = getLevelWidth();
        BodyDef wallDef = new BodyDef();
        wallDef.type = BodyDef.BodyType.KinematicBody;
        wallDef.position.set(currentLevelWidth/2,-40);
        bottomWall = world.createBody(wallDef);
        wallDef.position.set(currentLevelWidth/2,Bounce.height+240);
        topWall = world.createBody(wallDef);
        wallDef.position.set(-70,(Bounce.height+200)/2);
        leftWall = world.createBody(wallDef);
        wallDef.position.set(currentLevelWidth+70,(Bounce.height+200)/2);
        rightWall = world.createBody(wallDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(currentLevelWidth+34, 0f);
        Fixture bottomWallFix = bottomWall.createFixture(groundBox, 0f);
        groundBox.setAsBox(currentLevelWidth+34, 0f);
        Fixture topWallFix = topWall.createFixture(groundBox, 0f);
        groundBox.setAsBox(0f, Bounce.height+25);
        Fixture leftWallFix = leftWall.createFixture(groundBox, 0f);
        groundBox.setAsBox(0f, Bounce.height+25);
        Fixture rightWallFix = rightWall.createFixture(groundBox, 0f);
        bottomWallFix.setUserData(WALL);
        topWallFix.setUserData(WALL);
        leftWallFix.setUserData(WALL);
        rightWallFix.setUserData(WALL);
    }

    public void render(){
            world.step(1.0f / 60f, 6, 2);
            Bounce.collision.collisionHandler();
            for (GameObject g : gameObjects) {
                g.update();
            }
    }

}
