package com.gishump.bounce;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class Bounce extends ApplicationAdapter {
    public enum status {RUNNING, PAUSED, WON, LOST};
    public static status state;
    public static int height, width;
    public static Collision collision;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private RequestHandler androidHandler;
    private InputProcessor ip;
    private Level level;
    public static Ball ball;
    private Preferences prefs;
    private int attempts = 0;
    private int currentLevel = 0;
    private int maxLevel = 0;
    private int levelsPaidFor = 30;
    private boolean nagMessage;

    public Bounce(RequestHandler handler) {
        androidHandler = handler;
    }

	@Override
	public void create () {
        width = Gdx.graphics.getWidth() / 5;
        height = Gdx.graphics.getHeight() / 5;
        camera = new OrthographicCamera(width, height);
        debugRenderer = new Box2DDebugRenderer();
        camera.position.set(width * .5f, height * .5f, 0);
        camera.update();
        prefs = Gdx.app.getPreferences("Preferences");
        collision = new Collision();
        level = new Level();
        ip = new Input(level,androidHandler);
        Level.world.setContactListener(collision);
        loadUserData();
        Gdx.input.setInputProcessor(ip);
        state = status.RUNNING;
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(Level.world, camera.combined);
        if (state == status.RUNNING) { level.render(); }
        else if (state == status.PAUSED) { }
        else if (state == status.WON) { playerWins(); }
        else if (state == status.LOST) { playerLoses(); }
    }

    public void loadUserData() {
        nagMessage = prefs.getBoolean("nagMessage", false);
        if (!nagMessage) {
            currentLevel = prefs.getInteger("currentLevel", 1);
            attempts = prefs.getInteger("level"+currentLevel+"Attempts", 0);
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
        resetBall();
        androidHandler.levelCompleteDialog(new DialogResult() {
            @Override
            public void yes(int a) {
                if (maxLevel + 1 < androidHandler.getLevelsPaidFor()) {
                    maxLevel++;
                    prefs.putInteger("maxLevel", maxLevel);
                    prefs.putInteger("level" + currentLevel + "Attempts", attempts);
                    currentLevel++;
                    prefs.putInteger("currentLevel", currentLevel);
                    prefs.flush();
                    level.loadLevel(currentLevel);
                } else {
                    prefs.putBoolean("nagMessage", true);
                    prefs.flush();
                    nagUser();
                }
            }

            @Override
            public void no() {
                attempts=0;
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
        if (ball != null) { Level.world.destroyBody(ball.getBody()); }
        ball = null;
    }

    @Override
    public void dispose() {
        level.clearLevel();
        debugRenderer.dispose();
    }
}