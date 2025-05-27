package com.svalero.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.managers.R;
import com.svalero.game.projectiles.Projectile;
import com.svalero.game.projectiles.Ray;
import com.svalero.game.utils.DrawInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Frigate extends Character{

    private DrawInfo body, engine;
    private Animation<TextureRegion> bodyAnimation, engineAnimation;
    private TextureRegion bodyIdle;
    private boolean inPosition;
    private float targetY;

    public Frigate(){
        //Init
        speed = FRIGATE_SPEED;
        bulletSpeed = FRIGATE_RAY_SPEED;
        bulletDamage = FRIGATE_RAY_DAMAGE;
        fireRate = FRIGATE_FIRE_RATE;
        hitPoints = FRIGATE_HIT_POINTS;
        lives = 1;
        status = STATUS.ACTIVE;
        type = CHARACTER_TYPE.FRIGATE;
        pointsScore = FRIGATE_POINTS_SCORE;
        position = new Vector2();
        animationTime = 0;
        bodyIdle = R.getEnemyTexture(FRIGATE);
        inPosition = false;
        lastShot = 0;

        //Load textures
        engine = new DrawInfo();

        Array<TextureRegion> frames = R.getEnemiesRegions(FRIGATE_ENGINE);
        engineAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        frames = R.getEnemiesRegions(FRIGATE_WEAPON);
        bodyAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        //Random coordinate x, y out of screen
        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();
        position.y = screenHeight;
        float max = screenWidth - 80;
        float min = 80;
        position.x = MathUtils.random(min, max + 1);

        targetY = screenHeight - MathUtils.random(100f, 400f);

        //Update body, engine with start coordinates
        TextureRegion frame = engineAnimation.getKeyFrame(0);
        body = new DrawInfo(bodyIdle, position.x, position.y,
            bodyIdle.getRegionWidth() * FRIGATE_SCALE, bodyIdle.getRegionHeight() * FRIGATE_SCALE);
        engine = new DrawInfo(frame, position.x, position.y + bodyIdle.getRegionHeight(),
            frame.getRegionWidth() * FRIGATE_SCALE, frame.getRegionHeight() * FRIGATE_SCALE);

        //Init hit box
        hitBox = new Rectangle();
    }

    @Override
    public void update(float dt){
        animationTime += dt;

        engine.setRegion(engineAnimation.getKeyFrame(animationTime));

        if(hitEffect){
            hitEffectTime += dt;
            if (hitEffectTime >= ENEMY_HIT_EFFECT_DURATION) {
                hitEffect = false;
                hitEffectTime = 0;
            }
        }

        if (!inPosition) {
            position.y -= dt * speed;

            //Set in position
            if (position.y <= targetY) {
                inPosition = true;
                body.setRegion(bodyAnimation.getKeyFrame(animationTime));
                //Shoot immediately
                lastShot = TimeUtils.nanoTime() / 1_000_000_000f - FRIGATE_FIRE_RATE;
            }
        }else
            body.setRegion(bodyAnimation.getKeyFrame(animationTime));

        //Update body position
        body.setX(position.x);
        body.setY(position.y);

        //Calculate engine position
        float bodyCenterX = body.getX() + body.getWidth() / 2f;
        float bodyTopY = body.getY() + body.getHeight();

        float engineWidth = engine.getRegion().getRegionWidth() * FRIGATE_SCALE;
        float engineHeight = engine.getRegion().getRegionHeight() * FRIGATE_SCALE;

        //Remove space between engine effect and body
        engine.setX(bodyCenterX - engineWidth / 2f);
        engine.setY(bodyTopY - engineHeight);
        engine.setWidth(engineWidth);
        engine.setHeight(engineHeight);

        hitBox.set(position.x, position.y, body.getWidth(), body.getHeight());
    }

    public Projectile createProjectile(Vector2 rangerPosition){
        if(!inPosition) return null;
        float currentTime = TimeUtils.nanoTime() / 1_000_000_000f;
        //Shoot?
        if (currentTime - lastShot >= fireRate) {
            float centerX = body.getWidth() / 2f;
            Vector2 origin = new Vector2(
                position.x + centerX,
                position.y
            );
            lastShot = currentTime;
            return new Ray(origin, bulletSpeed, fireRate, bulletDamage, rangerPosition);
        }
        return null;
    }
}
