package com.svalero.game.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.managers.R;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Fighter extends Character {

    private Animation<TextureRegion> leftAnimation, rightAnimation;
    private TextureRegion idleFrame;

    private float lastMovementTime;
    private FORMATION formation;
    private FighterSquadron squadron;

    public Fighter() {
        //Init
        hitPoints = FIGHTER_HIT_POINTS;
        type = CHARACTER_TYPE.FIGHTER;
        status = STATUS.ACTIVE;
        lives = 1;
        pointsScore = FIGHTER_POINTS_SCORE;

        // Load textures
        idleFrame = R.getEnemyTexture(FIGHTER_IDLE);
        currentFrame = idleFrame;

        Array<TextureRegion> frames = R.getEnemiesRegions(FIGHTER_LEFT);
        leftAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        frames = R.getEnemiesRegions(FIGHTER_RIGHT);
        rightAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        animationTime = 0f;
        speed = FIGHTER_SPEED;
        lastMovementTime = 0f;
        // Set hit box
        hitBox = new Rectangle();
    }

    public void updateHitBox() {
        float width = currentFrame.getRegionWidth();
        float height = currentFrame.getRegionHeight();

        hitBox.set(
            position.x - width / 2f,
            position.y - height / 2f,
            width,
            height
        );
    }

    public void updateHitEffect(float dt){
        if(hitEffect){
            hitEffectTime += dt;
            if (hitEffectTime >= ENEMY_HIT_EFFECT_DURATION) {
                hitEffect = false;
                hitEffectTime = 0;
            }
        }
    }

    public void setAnimationByDirection(Vector2 direction, float dt) {
        animationTime += dt;

        if (direction.x < 0) {
            currentFrame = leftAnimation.getKeyFrame(animationTime, false);
        } else {
            currentFrame = rightAnimation.getKeyFrame(animationTime, false);
        }
    }

    public void setIdle() {
        currentFrame = idleFrame;
    }

}
