package com.svalero.game.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Projectile {

    protected TextureRegion texture;
    protected Vector2 position;

    protected float speed;
    protected float fireRate;
    protected float damage;



    public Projectile(Vector2 position, float speed, float fireRate, float damage) {
        this.position = position;
        this.speed = speed;
        this.fireRate = fireRate;
    }

    public Projectile(Vector2 position){
        this.position = position;
    }

    public abstract void update(float dt);
}
