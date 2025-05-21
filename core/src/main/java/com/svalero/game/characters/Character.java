package com.svalero.game.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
    protected Vector2 position;
    protected Rectangle hitBox;

    // Character animation
    protected Animation<TextureRegion> animation;
    protected float stateTime;
    protected TextureRegion currentFrame;

    public Character(float x, float y, float speed) {
        this.position.x = x;
        this.position.y = y;
        this.speed = speed;
    }

    public Character(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    // Get hit
    public void hit(float damage) {
        hitPoints -= damage;
        if(hitPoints <= 0) {
            lives--;
        }
    }

    @Override
    public void dispose() {
        hitBox = null;
        animation = null;
    }
}
