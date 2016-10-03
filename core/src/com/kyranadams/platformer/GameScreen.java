package com.kyranadams.platformer;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

public abstract class GameScreen extends AbstractScreen implements GestureDetector.GestureListener {

    private static final int SCREEN_WIDTH = 960;
    private static final int SCREEN_HEIGHT = 640;
    private static final boolean DISPLAY_FPS = true;

    protected OrthographicCamera camera;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private BitmapFont font;


    public GameScreen(Game game) {
        super(game);
        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
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
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderScene(batch, delta);
        batch.end();


        hudBatch.getProjectionMatrix().setToOrtho2D(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        hudBatch.begin();
        renderHud(hudBatch, delta);
        if (DISPLAY_FPS) {
            font.draw(hudBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 0, SCREEN_HEIGHT);
        }
        hudBatch.end();
    }

    protected abstract void renderScene(SpriteBatch batch, float delta);

    protected abstract void renderHud(SpriteBatch hudBatch, float delta);

    @Override
    public void hide() {
        Gdx.app.debug("Cuboc", "dispose game screen");
        batch.dispose();
        font.dispose();
    }
}
