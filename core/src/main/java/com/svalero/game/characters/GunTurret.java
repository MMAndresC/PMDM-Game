package com.svalero.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.managers.R;
import com.svalero.game.managers.SoundManager;
import com.svalero.game.projectiles.Missile;
import com.svalero.game.projectiles.Projectile;
import com.svalero.game.utils.DrawInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GunTurret extends Character{

    DrawInfo body, gun, mount;

    int direction;


    public GunTurret(){
        //Init
        hitPoints = GUN_TURRET_HIT_POINTS;
        type = CHARACTER_TYPE.GUN_TURRET;
        animationTime = 0;
        fireRate = GUN_TURRET_FIRE_RATE;
        lastShot = TimeUtils.nanoTime() / 1_000_000_000f;
        bulletDamage = GUN_TURRET_MISSILE_DAMAGE;
        bulletSpeed = GUN_TURRET_MISSILE_SPEED;
        status = STATUS.ACTIVE;
        lives = 1;
        pointsScore = GUN_TURRET_POINTS_SCORE;
        scale = GUN_TURRET_SCALE;

        //Load textures
        //Random side of screen, 0 left, 1 right
        //Default texture is left to right
        direction = MathUtils.random(0, 1);
        body = new DrawInfo();
        gun = new DrawInfo();
        mount = new DrawInfo();
        body.setRegion((direction == 0)
            ? R.getEnemyTexture(GUN_TURRET_LEFT)
            :R.getEnemyTexture(GUN_TURRET_RIGHT));
        gun.setRegion((direction == 0)
            ? R.getEnemyTexture(GUN_TURRET_GUN_LEFT)
            :R.getEnemyTexture(GUN_TURRET_GUN_RIGHT));
        mount.setRegion(R.getEnemyTexture(GUN_TURRET_MOUNT));


        //Show bigger
        body.setWidth(body.getRegion().getRegionWidth() * GUN_TURRET_SCALE);
        body.setHeight(body.getRegion().getRegionHeight() * GUN_TURRET_SCALE);
        gun.setWidth(gun.getRegion().getRegionWidth() * GUN_TURRET_SCALE);
        gun.setHeight(gun.getRegion().getRegionHeight() * GUN_TURRET_SCALE);
        mount.setWidth(mount.getRegion().getRegionWidth() * GUN_TURRET_SCALE);
        mount.setHeight(mount.getRegion().getRegionHeight() * GUN_TURRET_SCALE);

        //Initial position out of screen y
        float spacing = 80f;
        float x = (direction == 0)
            ? spacing
            : Gdx.graphics.getWidth() - mount.getRegion().getRegionWidth() - spacing;
        float y = Gdx.graphics.getHeight() + mount.getRegion().getRegionHeight();
        position = new Vector2(x,y);

        //Init hit box
        hitBox = new Rectangle();
    }

    @Override
    public void update(float dt) {
        animationTime += dt;
        //Update position, only y movement
        position.y -= GUN_TURRET_SPEED;

        if(hitEffect){
            hitEffectTime += dt;
            if (hitEffectTime >= ENEMY_HIT_EFFECT_DURATION) {
                hitEffect = false;
                hitEffectTime = 0;
            }
        }

        if(position.y < -mount.getRegion().getRegionHeight()){
            status = STATUS.OUT;
            return;
        }
        mount.setX(position.x);
        mount.setY(position.y);

        float mountWidth = mount.getRegion().getRegionWidth() * GUN_TURRET_SCALE;
        float mountHeight = mount.getRegion().getRegionHeight() * GUN_TURRET_SCALE;
        float bodyWidth = body.getRegion().getRegionWidth() * GUN_TURRET_SCALE;
        float bodyHeight = body.getRegion().getRegionHeight() * GUN_TURRET_SCALE;
        float gunHeight = gun.getRegion().getRegionHeight() * GUN_TURRET_SCALE;

        // Center body horizontally on mount
        float bodyX;
        if(direction == 0){
            bodyX = position.x + (mountWidth - bodyWidth) / 2f;
        }else{
            bodyX = position.x + (bodyWidth / 4f);
        }
        float bodyY = position.y + (mountHeight - bodyHeight) / 2f;
        body.setX(bodyX);
        body.setY(bodyY);

        // Center gun on body
        float adjust = 7;
        float gunX = (direction == 0)
            ? bodyX + (bodyWidth) / 2f - 4
            : bodyX - (bodyWidth) / 2f;
        float gunY = bodyY + (bodyHeight - gunHeight) / 2f - adjust;
        gun.setX(gunX);
        gun.setY(gunY);

        //Calculate new rectangle hit box
        float left = Math.min(mount.getX(), body.getX());
        float right = Math.max(mount.getX() + mountWidth, body.getX() + bodyWidth);
        float top = Math.max(mount.getY() + mountHeight, body.getY() + bodyHeight);
        float bottom = Math.min(mount.getY(), body.getY());

        //Update hit box
        if(direction == 0)
            hitBox.set(left, bottom, right - left, top - bottom);
        else
            hitBox.set(left - adjust * 2, bottom, right - left - adjust, top - bottom);
    }

    public Projectile createMissile(Vector2 rangerPosition){
        float currentTime = TimeUtils.nanoTime() / 1_000_000_000f;
        //Shoot?
        if (currentTime - lastShot >= fireRate) {

            //Sound Missile
            SoundManager.play(MISSILE_SOUND, LOW_SOUND_VOLUME);

            lastShot = currentTime;
            float originX = (direction == 0)
                ? gun.getX() + (gun.getWidth() / 2f)
                : gun.getX() - (gun.getWidth() / 2f);
            float originY = gun.getY() + gun.getHeight() / 2f - 10f;

            Vector2 origin = new Vector2(originX, originY);
            return new Missile(origin, bulletSpeed, bulletDamage, rangerPosition);
        }
        return null;
    }
}
