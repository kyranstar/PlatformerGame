package com.kyranadams.platformer;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SplashScreen extends AbstractScreen {

    TextureRegion title;
    SpriteBatch batch;
    float time = 0;

    public SplashScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        title = new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")), 0, 0, 480, 320);
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 320);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float alpha = 1;
        time += delta;
        if (time > 1) {
            alpha = 2 - time;
            if (time > 2) {
                game.setScreen(new MainMenuScreen(game));
            }
        }

        batch.begin();
        batch.draw(title, 0, 0);
        batch.setColor(1, 1, 1, alpha);
        batch.end();
    }

    @Override
    public void hide() {
        Gdx.app.debug("Platformer", "dispose main menu");
        batch.dispose();
        title.getTexture().dispose();
    }
}
