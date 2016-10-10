package com.kyranadams.platformer.scenes.entity;

import com.badlogic.gdx.graphics.g2d.Batch;

public class ControllableCharacter extends com.kyranadams.platformer.scenes.entity.Entity {
    public ControllableCharacter(String img) {
        super(img);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if (velocity.x < 0) {
            sprite.setFlip(true, false);
        } else if (velocity.x > 0) {
            sprite.setFlip(false, false);
        }
        sprite.draw(batch);
    }
}
