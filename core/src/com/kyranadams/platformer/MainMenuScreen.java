package com.kyranadams.platformer;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenuScreen extends AbstractScreen {

    private static final int SCREEN_WIDTH = 640;
    private static final int SCREEN_HEIGHT = 960;
    SpriteBatch batch;
    GlyphLayout glyphLayout;
    private BitmapFont font;
    float time = 0;

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        glyphLayout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        String menuText = "Tap to continue";
        glyphLayout.setText(font, menuText);

        batch.begin();
        font.draw(batch, "Tap to continue", SCREEN_WIDTH / 2 - glyphLayout.width / 2, SCREEN_HEIGHT / 2 - glyphLayout.height / 2);
        batch.end();

        time += delta;
        if (Gdx.input.justTouched()) {
            game.setScreen(new com.kyranadams.platformer.scenes.FirstScene(game));
        }

    }

    @Override
    public void hide() {
        Gdx.app.debug("Cubocy", "dispose intro");
        batch.dispose();
    }

}

