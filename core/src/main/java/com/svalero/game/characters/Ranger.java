package com.svalero.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.managers.R;
import com.svalero.game.managers.SoundManager;
import com.svalero.game.projectiles.ProjectileRanger;
import com.svalero.game.utils.DrawInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Ranger extends Character{

    private List<ProjectileRanger> projectiles;
    private float score;
    private boolean isMoving;

    private float rangerHeight;
    private float rangerWidth;

    private DrawInfo body, engine, engineEffect, shield;

    private Animation<TextureRegion> engineEffectAnimation;
    private Animation<TextureRegion> engineEffectPoweringAnimation;
    private Animation<TextureRegion> shieldAnimation;

    private TextureRegion fullHealth, mediumHealth, lowHealth;

    //Immune when lose life
    private boolean isImmune;
    private float immuneTime;
    private float immuneDuration;

    //Power ups
    private boolean shieldActive;
    private float pointsLifeShield;
    private boolean doubleCannonActive;

    public Ranger() {
        //Init
        projectiles = new ArrayList<ProjectileRanger>();
        score = 0f;
        isMoving = false;
        type = CHARACTER_TYPE.RANGER;
        isImmune = false;
        immuneTime = 0;
        lives = RANGER_LIVES;
        status = STATUS.ACTIVE;
        immuneDuration = 0;
        hitPoints = RANGER_HIT_POINTS;
        shieldActive = false;
        pointsLifeShield = RANGER_HIT_POINTS;
        doubleCannonActive = false;
        scale = RANGER_SCALE;

        //Ammo data
        fireRate = RANGER_FIRE_RATE;
        bulletDamage = RANGER_BULLET_DAMAGE;
        bulletSpeed = RANGER_BULLET_SPEED;
        lastShot = TimeUtils.nanoTime() / 1_000_000_000f;

        //Load textures
        fullHealth = R.getRangerTexture(RANGER_FULL_HEALTH);
        mediumHealth = R.getRangerTexture(RANGER_SLIGHT_DAMAGED);
        lowHealth = R.getRangerTexture(RANGER_VERY_DAMAGED);
        body = new DrawInfo();
        engine = new DrawInfo();
        engineEffect = new DrawInfo();
        shield = new DrawInfo();
        body.setRegion(fullHealth);
        engine.setRegion(R.getRangerTexture(RANGER_ENGINE));
        //Load animations
        Array<TextureRegion> frames = R.getRangerRegions(RANGER_ENGINE_EFFECTS_IDLE);
        engineEffectAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        engineEffect.setRegion(engineEffectAnimation.getKeyFrame(0f));

        frames = R.getRangerRegions(RANGER_ENGINE_EFFECTS_POWERING);
        engineEffectPoweringAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        animationTime = 0f;

        frames = R.getRangerRegions(RANGER_SHIELD);
        shieldAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        shield.setRegion(engineEffectAnimation.getKeyFrame(0f));

        //Set ranger in screen
        this.position = new Vector2(
            Gdx.graphics.getWidth() / 2f - body.getRegion().getRegionWidth() / 2f,
            Gdx.graphics.getHeight() / 3f - body.getRegion().getRegionHeight() / 2f
        );

        //Calculate ranger dimensions
        rangerHeight = ( engine.getRegion().getRegionHeight() + body.getRegion().getRegionHeight()) * RANGER_SCALE;
        rangerWidth = body.getRegion().getRegionWidth() * RANGER_SCALE;

        hitBox = new Rectangle();
    }

    @Override
    public void update(float dt) {
        updateProjectiles(dt);
        if(status == STATUS.DESTROYED) return;
        animationTime += dt;
        //Adjust textures to form ranger, three separate parts
        float overlap =  2f * RANGER_SCALE;

        float x = position.x - rangerWidth / 2f;
        float y = position.y;

        float engineEffectHeight = engineEffect.getRegion().getRegionHeight() * RANGER_SCALE;
        float engineHeight = engine.getRegion().getRegionHeight() * RANGER_SCALE;
        float bodyHeight = body.getRegion().getRegionHeight() * RANGER_SCALE;

        float yOffset = y;

        // 1. Engine effect
        TextureRegion effect;
        if(isMoving){
            effect = engineEffectPoweringAnimation.getKeyFrame(animationTime);
            engineEffect.update(effect, x + 20f, yOffset + 5f, rangerWidth * 0.6f, engineEffectHeight * 1.5f);
        } else {
            effect = engineEffectAnimation.getKeyFrame(animationTime);
            engineEffect.update(effect, x + 20f, yOffset + 20f, rangerWidth * 0.6f, engineEffectHeight * 1.5f);
        }

        yOffset += engineEffectHeight - overlap;
        overlap = 38f * RANGER_SCALE;

        // 2. Engine
        engine.update(engine.getRegion(), x, yOffset, rangerWidth, engineHeight);

        yOffset += engineHeight - overlap;

        // 3. Body
        body.update(body.getRegion(),x, yOffset,rangerWidth,bodyHeight);

        // 4. Shield animation
        if (shieldActive && shieldAnimation != null) {
            TextureRegion shieldFrame = shieldAnimation.getKeyFrame(animationTime);
            shield.update(shieldFrame, x, yOffset, rangerWidth, bodyHeight);
        }

        //Calculate rectangle hit box, small, adjust to body
        float hitBoxY = position.y + engineEffectHeight + (10f * RANGER_SCALE);
        float hitBoxHeight = (engineHeight + bodyHeight - (40f * RANGER_SCALE)) * 0.62f; //Init in 2/3 and down number adjusting
        float hitBoxWidth = rangerWidth * 0.46f; //Init in 1/3 and up number adjusting
        //Update hitBox
        hitBox.set(x + 25f, hitBoxY, hitBoxWidth, hitBoxHeight);

        //Calculate immunity duration
        if (isImmune) {
            immuneTime += dt;
            if (immuneTime >= immuneDuration) {
                isImmune = false;
                immuneTime = 0;
            }
        }
    }

    public void setNewPosition(float x, float y, boolean isMoving){
        this.isMoving = isMoving;
        //Control screen limits so that ranger does not go out of the screen
        float minX = rangerWidth / 2f;
        float maxX = Gdx.graphics.getWidth() - rangerWidth / 2f;

        float minY = 0;
        float maxY = Gdx.graphics.getHeight() - rangerHeight;

        position = new Vector2(
            Math.max(minX, Math.min(x, maxX)),
            Math.max(minY, Math.min(y, maxY))
        );
    }

    public void createProjectile(){
        //Spacing out shots
        float currentTime = TimeUtils.nanoTime() / 1_000_000_000f; // Seconds
        if (currentTime - lastShot >= fireRate) {
            //Sound shoot
            SoundManager.play(RANGER_BEAM_SOUND, LOW_SOUND_VOLUME);
            lastShot = currentTime;

            float posX = position.x - 2f;
            float posY = position.y + (rangerHeight / 2);

            if(doubleCannonActive){
                int OFFSET = 10;
                projectiles.add(
                    new ProjectileRanger(
                        new Vector2(posX - OFFSET, posY), bulletSpeed, fireRate, bulletDamage
                    )
                );
                projectiles.add(
                    new ProjectileRanger(
                        new Vector2(posX + OFFSET, posY), bulletSpeed, fireRate, bulletDamage
                    )
                );
            }else{
                projectiles.add(
                    new ProjectileRanger(
                        new Vector2(posX, posY), bulletSpeed, fireRate, bulletDamage
                    )
                );
            }
        }
    }

    public void lostLife(float immuneDuration){
        lives--;
        if(lives == 0){
            status = STATUS.DESTROYED;
            hitPoints = 0;
        }
        else {
            this.immuneDuration = immuneDuration;
            isImmune = true;
        }
    }

    public void updateProjectiles(float dt){
        for(ProjectileRanger projectile : projectiles){
            projectile.update(dt);
        }
    }

    // Get hit
    @Override
    public void hit(float damage) {
        hitPoints -= damage;
        if(hitPoints <= 0) {
            lives--;
            if(lives > 0) {
                body.setRegion(fullHealth);
                hitPoints = RANGER_HIT_POINTS;
                immuneDuration = RANGER_IMMUNITY_DURATION;
                isImmune = true;
            } else {
                status = STATUS.DESTROYED;
                hitPoints = 0;
            }
        }else{
            if(hitPoints <= RANGER_HIT_POINTS / 2f){
                body.setRegion(lowHealth);
            }else body.setRegion(mediumHealth);
            immuneDuration = RANGER_IMMUNITY_HIT_DURATION;
            isImmune = true;
        }
    }

    public void hitShield(float damage){
        pointsLifeShield -= damage;
        if(pointsLifeShield <= 0)
            destroyShield();
    }

    public void sumScore(float points){
        score += points;
    }

    public Rectangle setLenientHitBox(){
        float expandX = hitBox.width * RANGER_LENIENT_PERCENTAGE;
        float expandY = hitBox.height * RANGER_LENIENT_PERCENTAGE;

        return new Rectangle(
            hitBox.x - expandX / 2f,
            hitBox.y - expandY / 2f,
            hitBox.width + expandX,
            hitBox.height + expandY
        );
    }

    public void setPowerUp(POWER_UP type){
        //Duplicated items give extra score points
        switch(type){
            case HEALTH -> { //Recover all health points
                if(hitPoints == RANGER_HIT_POINTS){
                    score += POWER_UP_DUPLICATED_BONUS_POINTS;
                }else{
                    hitPoints = RANGER_HIT_POINTS;
                    body.setRegion(fullHealth);
                }
            }
            case SHIELD -> { //Double health
                //Shield active and full health -> extra score points
                if(shieldActive && pointsLifeShield == RANGER_HIT_POINTS){
                    score += POWER_UP_DUPLICATED_BONUS_POINTS;
                    return;
                }
                //Shield active and damaged -> Shield recover full health
                if(shieldActive && pointsLifeShield < RANGER_HIT_POINTS){
                    pointsLifeShield = RANGER_HIT_POINTS;
                    return;
                }
                //Not shield yet -> Active shield
                shieldActive = true;
            }
            case DAMAGE -> { // Double shoot
                if(doubleCannonActive)
                    score += POWER_UP_DUPLICATED_BONUS_POINTS;
                doubleCannonActive = true;
            }
        }
    }

    public void destroyShield(){
        shieldActive = false;
        pointsLifeShield = RANGER_HIT_POINTS;
    }
}
