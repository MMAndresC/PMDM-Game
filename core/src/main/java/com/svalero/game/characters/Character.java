package com.svalero.game.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Character implements Disposable {

    //Movement speed
    protected float speed;

    //Projectile speed
    private float bulletSpeed;

    //Projectile damage
    private float bulletDamage;

    //Weapon fire rate
    private float fireRate;

    //Points of life
    private float hitPoints;

    private int lives;

    //Position
    protected float x;
    protected float y;
    private Rectangle rect;

    // Character animation
    protected Animation<TextureRegion> animation;
    protected float stateTime;
    protected TextureRegion currentFrame;

    public Character(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    // Get hit
    public void hit(float damage) {
        hitPoints -= damage;
        if(hitPoints <= 0) {
            lives--;
        }
    }

    // Draw character
    public abstract void draw(SpriteBatch batch);

    @Override
    public void dispose() {
        rect = null;
        animation = null;
    }
}
