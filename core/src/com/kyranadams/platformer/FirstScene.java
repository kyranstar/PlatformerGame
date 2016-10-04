package com.kyranadams.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class FirstScene extends GameScreen {
    private static final int SCROLL_SPEED = 5;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    Stage stage;
    ControllableCharacter mainCharacter = new ControllableCharacter("badlogic.jpg");

    public FirstScene(Game game) {
        super(game);
        tiledMap = new TmxMapLoader().load("sceneOneMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        stage = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        stage.getViewport().setCamera(new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT));
        mainCharacter.setPosition(SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        stage.addActor(mainCharacter);
    }

    @Override
    protected void update(float delta) {
        stage.act(delta);
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() > SCREEN_WIDTH / 2) {
                mainCharacter.moveBy(SCROLL_SPEED, 0);
            } else {
                mainCharacter.moveBy(-SCROLL_SPEED, 0);
            }
            stage.getCamera().position.set(mainCharacter.getX(), mainCharacter.getY(), 0);
            stage.getCamera().update();
        }
    }

    @Override
    public void dispose() {
        mainCharacter.dispose();
    }

    @Override
    protected void renderScene(SpriteBatch batch) {
        tiledMapRenderer.setView((OrthographicCamera) stage.getCamera());
        tiledMapRenderer.render();
        stage.draw();
    }

    @Override
    protected void renderHud(SpriteBatch hudBatch) {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

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
