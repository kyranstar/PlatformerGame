package com.kyranadams.platformer;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GameScreen extends AbstractScreen implements GestureDetector.GestureListener {

    protected static final int SCREEN_WIDTH = 240;
    protected static final int SCREEN_HEIGHT = 160;
    protected static final boolean DISPLAY_FPS = true;

    private SpriteBatch batch;
    protected BitmapFont font;
    protected Stage stage;

    protected static final int SHORT_PRESS_LENGTH_MILLIS = 300;


    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderScene(batch, stage.getCamera());
    }

    protected abstract void update(float delta);

    protected abstract void renderScene(SpriteBatch batch, Camera camera);

    @Override
    public void hide() {
        Gdx.app.debug("Cuboc", "dispose game screen");
        batch.dispose();
        font.dispose();
    }

    private long timePressed = -1;
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        timePressed = System.currentTimeMillis();
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        y *= (float) SCREEN_HEIGHT / Gdx.graphics.getHeight();
        x *= (float) SCREEN_WIDTH / Gdx.graphics.getWidth();

        if(timePressed > 0 && System.currentTimeMillis() - timePressed < SHORT_PRESS_LENGTH_MILLIS) {
            shortPress(x, y);
            timePressed = -1;
        }
        return true;
    }
    protected abstract void shortPress(float x, float y);

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }


    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
