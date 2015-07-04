package com.gishump.bounce;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class Bounce extends ApplicationAdapter {
    public enum status {RUNNING, PAUSED, WON, LOST, IDLE};
    public static status state;
    public static int height, width;
    public static int rawHeight, rawWidth;
    public static Collision collision;
    public static Ball ball;
    private Camera camera;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    private final RequestHandler androidHandler;
    private InputProcessor ip;
    private GestureDetector.GestureAdapter gd;
    private InputMultiplexer im;
    private Level level;
    private Preferences prefs;
    private int attempts = 1;
    private int currentLevel = 0;
    private int maxLevel = 0;
    private int levelsPaidFor = 30;
    private boolean nagMessage;
    //private FPSLogger fps;

    public Bounce(RequestHandler handler) {
        androidHandler = handler;
    }

	@Override
	public void create () {
        rawWidth = Gdx.graphics.getWidth();
        rawHeight = Gdx.graphics.getHeight();
        width = rawWidth / 5;
        height = rawHeight / 5;
        Gdx.graphics.setVSync(true);
        camera = new Camera(width, height);
        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        prefs = Gdx.app.getPreferences("Preferences");
        collision = new Collision();
        level = new Level(camera);
        ShapeRenderer shapeRenderer  = new ShapeRenderer();
        ip = new Input(level,androidHandler, camera);
        gd = new Gesture(camera);
        im = new InputMultiplexer(ip, new GestureDetector(gd));
        Level.world.setContactListener(collision);
        loadUserData();
        Gdx.input.setInputProcessor(im);
        //fps = new FPSLogger();
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(Level.world, camera.getCombinedMatrix());
        camera.update();
        if (state == status.RUNNING) { level.render(); drawMenuIcon(); }
        else if (state == status.PAUSED) { } // TODO Implement Dimming
        else if (state == status.IDLE) { camera.checkFinishedShowing(); drawMenuIcon(); }
        else if (state == status.WON) { playerWins(); }
        else if (state == status.LOST) { playerLoses(); drawMenuIcon(); }
    }

    public void loadUserData() {
        nagMessage = prefs.getBoolean("nagMessage", false);
        if (!nagMessage) {
            currentLevel = prefs.getInteger("currentLevel", 1);
            attempts = prefs.getInteger("level"+currentLevel+"Attempts", 1);
            maxLevel = prefs.getInteger("maxLevel", 0);
            level.loadLevel(currentLevel);
        }
        else {
            nagUser();
        }
    }

    public void nagUser() {
        if (androidHandler.arePurchasesAvailable()) {
            // TODO: Implement Purchasing Dialog
        }
        else {
            androidHandler.noPurchasesAvailable(new DialogResult() {
                @Override
                public void yes(int a) {
                    androidHandler.levelSelector(new DialogResult() {
                        @Override
                        public void yes(int a) {
                            prefs.putBoolean("nagMessage", false);
                            prefs.putInteger("currentLevel", a);
                            prefs.flush();
                            attempts = prefs.getInteger("level" + a + "Attempts", 1);
                            resetBall();
                            level.loadLevel(a);
                        }

                        @Override
                        public void no() {
                        }
                    });
                }

                @Override
                public void no() {
                    Gdx.app.exit();
                }
            });
        }
    }

    public void playerWins() {
        state = status.PAUSED;
        resetBall();
        androidHandler.levelCompleteDialog(new DialogResult() {
            @Override
            public void yes(int a) {
                if (maxLevel + 1 < androidHandler.getLevelsPaidFor()) {
                    maxLevel++;
                    prefs.putInteger("maxLevel", maxLevel);
                    prefs.putInteger("level" + currentLevel + "Attempts", 0);
                    currentLevel++;
                    prefs.putInteger("currentLevel", currentLevel);
                    prefs.flush();
                    level.loadLevel(currentLevel);
                    resetBall();
                    state = status.RUNNING;
                } else {
                    prefs.putBoolean("nagMessage", true);
                    prefs.flush();
                    nagUser();
                }
            }
            @Override
            public void no() {
                attempts=1;
                state = status.RUNNING;
            }
        },attempts);
    }

    public void playerLoses() {
        resetBall();
        attempts++;
        state = status.RUNNING;
    }

    public void resetBall() {
        if (ball != null) {
            Level.world.destroyBody(ball.getBody()); }
        ball = null;
    }

    public void drawMenuIcon() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(rawWidth-70, rawHeight-50,20,20);
        shapeRenderer.rect(rawWidth-70, rawHeight-80,20,20);
        shapeRenderer.rect(rawWidth-70, rawHeight-110,20,20);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        level.clearLevel();
        debugRenderer.dispose();
    }
}