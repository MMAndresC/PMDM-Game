package com.svalero.game.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.svalero.game.constants.Constants.STATUS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Projectile implements Disposable {

    protected TextureRegion currentFrame;
    protected Animation<TextureRegion> animation;
    protected float animationTime;

    protected Vector2 position;

    protected float speed;
    protected float fireRate;
    protected float damage;

    protected STATUS status;

    protected Rectangle rect;

    //Relation with shooter
    protected Character shooter;
    private FighterSquadron originSquadron;


    public Projectile(Character shooter, Vector2 position, float speed, float fireRate, float damage) {
        this.position = position;
        this.speed = speed;
        this.fireRate = fireRate;
        this.damage = damage;
        this.rect = new Rectangle();
        this.status = STATUS.ACTIVE;
        this.shooter = shooter;
    }

    public Projectile(Vector2 position, float speed, float fireRate, float damage) {
        this.position = position;
        this.speed = speed;
        this.fireRate = fireRate;
        this.damage = damage;
        this.rect = new Rectangle();
        this.status = STATUS.ACTIVE;
    }

    public Projectile(Character shooter, Vector2 position){
        this.position = position;
        this.rect = new Rectangle();
        this.status = STATUS.ACTIVE;
        this.shooter = shooter;
    }

    public Projectile(Character shooter, Vector2 position, float speed, float damage, TextureRegion currentFrame) {
        this.position = new Vector2(position);
        this.speed = speed;
        this.damage = damage;
        this.rect = new Rectangle();
        this.status = STATUS.ACTIVE;
        this.currentFrame = currentFrame;
        this.shooter = shooter;

        if (shooter instanceof Fighter fighter) {
            this.originSquadron = fighter.getSquadron();
        }
    }

    public abstract void update(float dt);

    public boolean isOutOfBounds(){
        return status == STATUS.OUT;
    }

    @Override
    public void dispose() {
        rect = null;
        animation = null;
    }
}
