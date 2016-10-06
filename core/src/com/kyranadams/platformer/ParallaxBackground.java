package com.kyranadams.platformer;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ParallaxBackground {
    protected Sprite sprite;
    private Texture tex;
    private float speed;

    public ParallaxBackground(Vector2 center, String img, float speed) {
        this.tex = new Texture(Gdx.files.internal(img));
        this.sprite = new Sprite(tex);
        sprite.setCenter(center.x, center.y);
        this.speed = speed;
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
    public void move(Vector2 amount){
        Vector2 scaledMove = amount.scl(speed);
        sprite.translate(-scaledMove.x, -scaledMove.y);
    }

    public void dispose(){
        tex.dispose();
    }
}
