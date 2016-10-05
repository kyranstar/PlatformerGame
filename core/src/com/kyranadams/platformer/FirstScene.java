package com.kyranadams.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class FirstScene extends GameScreen {
    private static final int SCROLL_SPEED = 100;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private ControllableCharacter mainCharacter = new ControllableCharacter("george.png");
    private static final int[] RENDERED_LAYERS = new int[]{0};
    private static final float FRICTION = 0.0001f;

    public FirstScene(Game game) {
        super(game);
        // load map
        tiledMap = new TmxMapLoader().load("sceneOneMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // load character position
        Vector2 characterStartPos = ((RectangleMapObject) tiledMap.getLayers().get("Entities").getObjects().get("George")).getRectangle().getPosition(new Vector2());
        mainCharacter.setPosition(characterStartPos.x, characterStartPos.y);

        // create stage
        stage = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        stage.addActor(mainCharacter);
    }

    @Override
    protected void update(float delta) {
        stage.act(delta);
        if (Gdx.input.isTouched()) {
            // scale input to viewport
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            stage.getCamera().unproject(touch);

            // if right hand side, move right
            if (touch.x > stage.getCamera().viewportWidth / 2) {
                mainCharacter.velocity = new Vector2(SCROLL_SPEED, 0);
            } else {
                mainCharacter.velocity = new Vector2(-SCROLL_SPEED, 0);
            }

        }
        mainCharacter.update(delta);
        // apply friction
        mainCharacter.velocity.scl((float) Math.pow(FRICTION, delta));
        updateCamera(delta);
    }

    private void updateCamera(float delta) {
        // camera follows player
        stage.getCamera().position.set(mainCharacter.getX(), mainCharacter.getY(), 0);

        // Camera borders
        Vector2 camMin = new Vector2(stage.getCamera().viewportWidth / 2, stage.getCamera().viewportHeight / 2);
        Vector2 camMax = new Vector2(tileMapWidthPixels() - stage.getCamera().viewportWidth / 2, tileMapHeightPixels() - stage.getCamera().viewportHeight / 2);

        float camX = stage.getCamera().position.x;
        float camY = stage.getCamera().position.y;
        //keep camera within borders
        camX = Math.min(camMax.x, Math.max(camX, camMin.x));
        camY = Math.min(camMax.y, Math.max(camY, camMin.y));

        stage.getCamera().position.set(camX, camY, stage.getCamera().position.z);
        stage.getCamera().update();
    }

    private int tileMapWidthPixels() {
        return tiledMap.getProperties().get("width", Integer.class) * tiledMap.getProperties().get("tilewidth", Integer.class);
    }

    private int tileMapHeightPixels() {
        return tiledMap.getProperties().get("height", Integer.class) * tiledMap.getProperties().get("tileheight", Integer.class);
    }

    @Override
    public void dispose() {
        mainCharacter.dispose();
    }

    @Override
    protected void renderScene(SpriteBatch batch) {
        tiledMapRenderer.setView((OrthographicCamera) stage.getCamera());
        tiledMapRenderer.render(RENDERED_LAYERS);
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
