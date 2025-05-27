package com.svalero.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.managers.R;
import com.svalero.game.utils.DrawInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Kamikaze extends Character{

    private DrawInfo body, engineEffect;
    private boolean inPosition = false;

    public Kamikaze(Vector2 rangerPosition) {
        //Init
        hitPoints = KAMIKAZE_HIT_POINTS;
        animationTime = 0;
        speed = KAMIKAZE_SPEED;
        type = CHARACTER_TYPE.KAMIKAZE;
        status = STATUS.ACTIVE;
        lives = 1;
        pointsScore = KAMIKAZE_POINTS_SCORE;

        //Load textures
        TextureRegion texture =  R.getEnemyTexture(KAMIKAZE);
        body = new DrawInfo( texture, rangerPosition.x, 200f,
            texture.getRegionWidth() * KAMIKAZE_SCALE,
            texture.getRegionHeight() * KAMIKAZE_SCALE);

        Array<TextureRegion> frames = R.getEnemiesRegions(KAMIKAZE_ENGINE_EFFECTS);
        animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        texture = animation.getKeyFrame(0f);
        engineEffect = new DrawInfo( texture, rangerPosition.x, 200f,
            texture.getRegionWidth() * KAMIKAZE_SCALE,
            texture.getRegionHeight() * KAMIKAZE_SCALE);

        position = new Vector2(rangerPosition.x, Gdx.graphics.getHeight());

        hitBox = new Rectangle();
    }

    @Override
    public void update(float dt, Vector2 rangerPosition) {
        animationTime += dt;
        engineEffect.setRegion(animation.getKeyFrame(animationTime));

        if(hitEffect){
            hitEffectTime += dt;
            if (hitEffectTime >= ENEMY_HIT_EFFECT_DURATION) {
                hitEffect = false;
                hitEffectTime = 0;
            }
        }

        //Normalize direction, value less than 1
        Vector2 direction = new Vector2(rangerPosition).sub(position).nor();
        //Update position going against ranger
        position.mulAdd(direction, speed * dt);

        //Update body position
        body.setX(position.x);
        body.setY(position.y);

        //Center engine effect with body
        float bodyCenterX = body.getX() + body.getWidth() / 2f;
        float bodyTopY = body.getY() + body.getHeight();

        float engineWidth = engineEffect.getRegion().getRegionWidth() * KAMIKAZE_SCALE;
        float engineHeight = engineEffect.getRegion().getRegionHeight() * KAMIKAZE_SCALE;

        float overlap = 6f;

        //Remove spaces between engine effect and body
        engineEffect.setX(bodyCenterX - engineWidth / 2f);
        engineEffect.setY(bodyTopY - engineHeight - overlap);
        engineEffect.setWidth(engineWidth);
        engineEffect.setHeight(engineHeight);

        //Update hit box
        hitBox.set(position.x, position.y, body.getWidth(), body.getHeight());
    }


}
