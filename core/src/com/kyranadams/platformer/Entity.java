package com.kyranadams.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;


public class Entity extends Actor {
    protected Vector2 velocity;
    protected Sprite sprite;
    private Texture tex;

    public Entity(String img) {
        this.tex = new Texture(Gdx.files.internal(img));
        this.sprite = new Sprite(tex);
        this.setTouchable(Touchable.enabled);
        this.setBounds(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        this.velocity = new Vector2();
    }

    public void addVelocity(Vector2 v) {
        this.velocity.add(v.x, v.y);
    }

    public void update(float delta) {
        this.moveBy(velocity.x * delta, velocity.y * delta);
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
