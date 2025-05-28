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
import com.svalero.game.projectiles.Proton;
import com.svalero.game.utils.DrawInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.ArrayList;
import java.util.List;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Dreadnought extends Character{

    private DrawInfo body, engine;
    private final Animation<TextureRegion> bodyAnimation;
    private final Animation<TextureRegion> engineAnimation;
    private Vector2 targetPosition;

    public Dreadnought() {
        //Init
        type = CHARACTER_TYPE.DREADNOUGHT;
        status = STATUS.ACTIVE;
        speed = DREADNOUGHT_SPEED;
        hitPoints = DREADNOUGHT_HIT_POINTS;
        pointsScore = DREADNOUGHT_POINTS_SCORE;
        fireRate = DREADNOUGHT_FIRE_RATE;
        bulletDamage = DREADNOUGHT_PROTON_DAMAGE;
        bulletSpeed = DREADNOUGHT_PROTON_SPEED;
        lastShot = 0;
        animationTime = 0;
        body = new DrawInfo();
        engine = new DrawInfo();
        hitBox = new Rectangle();
        lives = 1;
        targetPosition = new Vector2();
        scale = DREADNOUGHT_SCALE;

        //Load animations
        Array<TextureRegion> frames = R.getEnemiesRegions(DREADNOUGHT_BODY);
        bodyAnimation =  new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        frames = R.getEnemiesRegions(DREADNOUGHT_ENGINE);
        engineAnimation =  new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        //Origin position center x & y out of screen
        float centerX = Gdx.graphics.getWidth() / 2f;
        float y = Gdx.graphics.getHeight();
        TextureRegion frame = bodyAnimation.getKeyFrame(0);
        float x = centerX - frame.getRegionWidth() / 2f;
        position = new Vector2(x, y);
        targetPosition = new Vector2(x, y - DREADNOUGHT_LIMIT_POSITION_Y);

       //Load texture
        body = new DrawInfo(frame, x, y, frame.getRegionWidth() * DREADNOUGHT_SCALE,
            frame.getRegionHeight() * DREADNOUGHT_SCALE);

        frame = engineAnimation.getKeyFrame(0f);
        engine = new DrawInfo( frame, x, y + body.getHeight(),
            frame.getRegionWidth() * DREADNOUGHT_SCALE,
            frame.getRegionHeight() * DREADNOUGHT_SCALE);

    }

    public void update(float dt) {
        animationTime += dt;
        body.setRegion(bodyAnimation.getKeyFrame(animationTime));
        engine.setRegion(engineAnimation.getKeyFrame(animationTime));

        if(hitEffect){
            hitEffectTime += dt;
            if (hitEffectTime >= ENEMY_HIT_EFFECT_DURATION) {
                hitEffect = false;
                hitEffectTime = 0;
            }
        }

        //Normalize direction, value less than 1
        Vector2 direction = new Vector2(targetPosition).sub(position).nor();
        //Update position
        position.mulAdd(direction, speed * dt);

        if (position.dst2(targetPosition) < 0.01f) {
            //New target position
            float maxOffsetY = 50f;
            float offsetY = MathUtils.random(-maxOffsetY, maxOffsetY);
            float maxX = Gdx.graphics.getWidth() - body.getWidth();
            float minX = body.getWidth() + 1;
            targetPosition.x = MathUtils.random(minX, maxX);
            float screenHeight = Gdx.graphics.getHeight();
            targetPosition.y = screenHeight - DREADNOUGHT_LIMIT_POSITION_Y + offsetY;
        }

        //Update body position
        body.setX(position.x);
        body.setY(position.y);

        //Calculate engine position
        float bodyCenterX = body.getX() + body.getWidth() / 2f;
        float bodyTopY = body.getY() + body.getHeight();

        float engineWidth = engine.getRegion().getRegionWidth() * DREADNOUGHT_SCALE;
        float engineHeight = engine.getRegion().getRegionHeight() * DREADNOUGHT_SCALE;

        //Remove space between engine effect and body
        engine.setX(bodyCenterX - engineWidth / 2f);
        engine.setY(bodyTopY - engineHeight);
        engine.setWidth(engineWidth);
        engine.setHeight(engineHeight);

        hitBox.set(position.x, position.y, body.getWidth(), body.getHeight());
    }

    public boolean destroyedByCollision(){
        hitPoints -= RANGER_HIT_POINTS;
        return hitPoints <= 0;
    }

    public List<Proton> createProjectile(){
        float currentTime = TimeUtils.nanoTime() / 1_000_000_000f;
        //Shoot?
        if (currentTime - lastShot >= fireRate) {
            float centerX = body.getWidth() / 2f;
            Vector2 origin = new Vector2(
                position.x + centerX,
                position.y
            );
            lastShot = currentTime;
            List<Proton> protons = new ArrayList<>();

            protons.add(new Proton(origin.cpy(), bulletSpeed, bulletDamage, DIRECTION_LEFT));
            protons.add(new Proton(origin.cpy(), bulletSpeed, bulletDamage, DIRECTION_CENTER));
            protons.add(new Proton(origin.cpy(), bulletSpeed, bulletDamage, DIRECTION_RIGHT));

            return protons;
        }
        return null;
    }
}
