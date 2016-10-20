package com.kyranadams.platformer.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kyranadams.platformer.CameraController;
import com.kyranadams.platformer.GameScreen;
import com.kyranadams.platformer.ParallaxBackground;
import com.kyranadams.platformer.scenes.dialog.Dialog;
import com.kyranadams.platformer.scenes.entity.ControllableCharacter;
import com.kyranadams.platformer.scenes.entity.Entity;

import java.util.HashMap;
import java.util.Map;


public class FirstScene extends GameScreen {
    private static final int SCROLL_SPEED = 100;
    private static final float GRAVITY = 200;

    private ParallaxBackground background;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private ControllableCharacter mainCharacter = new ControllableCharacter("Niles.png");
    private static final int[] RENDERED_LAYERS = new int[]{0};
    // tilemap size in tiles
    private final int TILEMAP_WIDTH;
    private final int TILEMAP_HEIGHT;
    // tile size in pixels
    private final int TILE_WIDTH;
    private final int TILE_HEIGHT;

    private Dialog dialogInControl;

    private TiledMapTileLayer collisionLayer;
    private CameraController cameraController;

    public FirstScene(Game game) {
        super(game);
        // load map
        tiledMap = new TmxMapLoader().load("sceneOneMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // load character position
        Vector2 characterStartPos = ((RectangleMapObject) tiledMap.getLayers().get("Entities").getObjects().get("George")).getRectangle().getPosition(new Vector2());
        mainCharacter.setPosition(characterStartPos.x, characterStartPos.y);

        TILEMAP_WIDTH = tiledMap.getProperties().get("width", Integer.class);
        TILEMAP_HEIGHT = tiledMap.getProperties().get("height", Integer.class);
        TILE_WIDTH = tiledMap.getProperties().get("tilewidth", Integer.class);
        TILE_HEIGHT = tiledMap.getProperties().get("tileheight", Integer.class);

        collisionLayer = ((TiledMapTileLayer) tiledMap.getLayers().get("Collisions"));

        this.background = new ParallaxBackground(new Vector2(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2), "space.jpg", .35f);

        // create stage
        stage = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        stage.addActor(mainCharacter);

        for (MapObject object : tiledMap.getLayers().get("Objects").getObjects()) {
            Entity ent = new Entity(object.getName());
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            ent.setPosition(rect.getX(), rect.getY());
            stage.addActor(ent);
        }
        this.cameraController = new CameraController(stage.getCamera(), TILE_WIDTH * TILEMAP_WIDTH, TILE_HEIGHT * TILEMAP_HEIGHT);
        Map<String, Entity> map = new HashMap<String, Entity>(){{
            put("niles", mainCharacter);
        }};

        dialogInControl = new Dialog(Gdx.files.internal("dialog/scene1/dialog.txt").readString(), map, stage, cameraController);
        dialogInControl.activate();
    }

    @Override
    protected void update(float delta) {
        Vector2 oldCamera = new Vector2(stage.getCamera().position.x, stage.getCamera().position.y);
        stage.act(delta);
        if (dialogInControl != null && !dialogInControl.isDone) {
            background.move(new Vector2(stage.getCamera().position.x, stage.getCamera().position.y).sub(oldCamera));
            return;
        }

        if (Gdx.input.isTouched()) {
            // if right hand side, move right
            if ((float) Gdx.input.getX() * SCREEN_WIDTH / Gdx.graphics.getWidth() > stage.getCamera().viewportWidth / 2) {
                mainCharacter.getVelocity().x = SCROLL_SPEED;
            } else {
                mainCharacter.getVelocity().x = -SCROLL_SPEED;
            }
        }
        mainCharacter.getVelocity().add(0, -GRAVITY * delta);
        mainCharacter.update(delta, collisionLayer);
        cameraController.centerCamera(mainCharacter.getX(), mainCharacter.getY(), delta);

        background.move(new Vector2(stage.getCamera().position.x, stage.getCamera().position.y).sub(oldCamera));
    }

    @Override
    public void dispose() {
        mainCharacter.dispose();
        background.dispose();
    }

    protected void renderScene(SpriteBatch batch) {
        tiledMapRenderer.setView((OrthographicCamera) stage.getCamera());
        tiledMapRenderer.render(RENDERED_LAYERS);
        stage.draw();
    }

    protected void renderHud(SpriteBatch batch) {
        if (dialogInControl != null) {
            dialogInControl.render(batch);
            return;
        }
    }

    protected void renderScene(SpriteBatch batch, Camera camera) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.begin();
        background.render(batch);
        batch.end();

        batch.setProjectionMatrix(camera.projection);
        batch.begin();
        renderScene(batch);
        batch.end();


        batch.getProjectionMatrix().setToOrtho2D(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.begin();
        renderHud(batch);
        if (DISPLAY_FPS) {
            font.draw(batch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 0, SCREEN_HEIGHT);
        }
        batch.end();
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
