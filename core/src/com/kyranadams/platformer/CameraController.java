package com.kyranadams.platformer;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by VishalNoonavath on 10/17/16.
 */
public class CameraController {
    private final int tileMapWidth;
    private final int tileMapHeight;
    private Camera camera;

    public CameraController(Camera camera, int tileMapWidth, int tileMapHeight){
        this.camera =camera;
        this.tileMapWidth = tileMapWidth;
        this.tileMapHeight = tileMapHeight;
    }

    public void centerCamera(float x, float y, float delta) {
        // camera follows player
        camera.position.set(x, y, 0);

        // Camera borders
        Vector2 camMin = new Vector2(camera.viewportWidth / 2, camera.viewportHeight / 2);
        Vector2 camMax = new Vector2(tileMapWidth - camera.viewportWidth / 2, tileMapHeight - camera.viewportHeight / 2);
    
        float camX = camera.position.x;
        float camY = camera.position.y;
        //keep camera within borders
        camX = Math.min(camMax.x, Math.max(camX, camMin.x));
        camY = Math.min(camMax.y, Math.max(camY, camMin.y));

        camera.position.set(camX, camY, camera.position.z);
        camera.update();
    }
}
