package com.svalero.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Projectile extends Character{

    protected Texture texture;

    public Projectile(float x, float y, float speed) {
        super(x, y, speed);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX(), getY());
    }

    public abstract void update(float dt);
}
