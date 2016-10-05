package com.kyranadams.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;

public class ControllableCharacter extends Entity {
    public ControllableCharacter(String img) {
        super(img);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if (velocity.x < 0) {
            sprite.setFlip(true, false);
        } else {
            sprite.setFlip(false, false);
        }
        sprite.draw(batch);
    }
}
