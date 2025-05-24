package com.svalero.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.constants.Constants;
import com.svalero.game.managers.R;

import static com.svalero.game.constants.Constants.GUN_TURRET_MISSILES;

public class Missile extends Projectile {

    private final Vector2 velocity;

    public Missile(Vector2 origin, float speed, float damage, Vector2 rangerPosition) {
        this.position = new Vector2(origin);
        this.speed = speed;
        this.damage = damage;
        this.animationTime = 0;

        //Vector from origin to target
        Vector2 direction = new Vector2(rangerPosition).sub(origin).nor();
        this.velocity = direction.scl(speed); // velocity = direction * scalar velocity
        //Animation missile, copy to not change general frames
        Array<TextureRegion> originalFrames = R.getEnemiesRegions(GUN_TURRET_MISSILES);
        Array<TextureRegion> frames = new Array<>();

        for (TextureRegion region : originalFrames) {
            TextureRegion copy = new TextureRegion(region);

            //Flip frames
            if (velocity.x < 0) {
                copy.flip(true, false);
            }
            frames.add(copy);
        }

        animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        currentFrame = animation.getKeyFrame(animationTime);
    }

    @Override
    public void update(float dt) {
        animationTime += dt;
        currentFrame = animation.getKeyFrame(animationTime);
        //Movement according to direction
        position.mulAdd(velocity, dt);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        //Change status of missiles out of screen
        if (position.x + currentFrame.getRegionWidth() < 0 || position.x > screenWidth ||
            position.y + currentFrame.getRegionHeight() < 0 || position.y > screenHeight) {
            status = Constants.STATUS.OUT;
            return;
        }

        rect = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }
}
