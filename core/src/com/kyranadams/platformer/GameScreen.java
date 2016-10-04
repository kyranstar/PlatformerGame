package com.kyranadams.platformer;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GameScreen extends AbstractScreen implements GestureDetector.GestureListener {

    protected static final int SCREEN_WIDTH = 960;
    protected static final int SCREEN_HEIGHT = 640;
    private static final boolean DISPLAY_FPS = true;

    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private BitmapFont font;
    protected Stage stage;


    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
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

        batch.setProjectionMatrix(stage.getCamera().projection);
        batch.begin();
        renderScene(batch);
        batch.end();


        hudBatch.getProjectionMatrix().setToOrtho2D(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        hudBatch.begin();
        renderHud(hudBatch);
        if (DISPLAY_FPS) {
            font.draw(hudBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 0, SCREEN_HEIGHT);
        }
        hudBatch.end();
    }

    protected abstract void update(float delta);

    protected abstract void renderScene(SpriteBatch batch);

    protected abstract void renderHud(SpriteBatch hudBatch);

    @Override
    public void hide() {
        Gdx.app.debug("Cuboc", "dispose game screen");
        batch.dispose();
        font.dispose();
    }
}
