package com.kyranadams.platformer;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GameScreen extends AbstractScreen implements GestureDetector.GestureListener {

    protected static final int SCREEN_WIDTH = 480;
    protected static final int SCREEN_HEIGHT = 320;
    protected static final boolean DISPLAY_FPS = true;

    private SpriteBatch batch;
    protected BitmapFont font;
    protected Stage stage;


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
}
