package com.kyranadams.platformer.scenes.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;


public class Entity extends Actor {
    protected Vector2 velocity;
    protected Sprite sprite;
    private Texture tex;
    private static final float FRICTION = 0.0001f;

    public Entity(String img) {
        this.tex = new Texture(Gdx.files.internal(img));
        this.sprite = new Sprite(tex);

        this.setTouchable(Touchable.enabled);
        this.setBounds(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        this.velocity = new Vector2();
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void addVelocity(Vector2 v) {
        this.velocity.add(v.x, v.y);
    }

    public void update(float delta, TiledMapTileLayer collisionLayer) {
        Vector2 oldPosition = new Vector2(getX(), getY());
        this.moveBy(velocity.x * delta, 0);
        if (tilemapCollision(collisionLayer, sprite.getBoundingRectangle())) {
            setPosition(oldPosition.x, oldPosition.y);
            velocity.x = 0;
        }

        oldPosition = new Vector2(getX(), getY());
        this.moveBy(0, velocity.y * delta);
        if (tilemapCollision(collisionLayer, sprite.getBoundingRectangle())) {
            setPosition(oldPosition.x, oldPosition.y);
            velocity.y = 0;
        }

        // apply friction
        velocity.x *= ((float) Math.pow(FRICTION, delta));
    }

    private boolean tilemapCollision(TiledMapTileLayer collisionLayer, Rectangle box) {
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();
        // get tile of each corner
        Vector2 bottomLeft = new Vector2(box.getX(), box.getY()).scl(1f / tileWidth, 1f / tileHeight);
        Vector2 topLeft = new Vector2(box.getX(), box.getY() + box.getHeight()).scl(1f / tileWidth, 1f / tileHeight);
        Vector2 bottomRight = new Vector2(box.getX() + box.getWidth(), box.getY()).scl(1f / tileWidth, 1f / tileHeight);

        for (int x = (int) bottomLeft.x; x < bottomRight.x; x++) {
            for (int y = (int) bottomLeft.y; y < topLeft.y; y++) {
                // if it's solid
                if (collisionLayer.getCell(x, y) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        sprite.draw(batch);
    }

    @Override
    public void positionChanged() {
        sprite.setPosition(getX(), getY());
    }

    public void dispose() {
        tex.dispose();
    }
}
