package com.svalero.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.game.managers.R;
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
        position.y -= GUN_TURRET_SPEED;
        mount.setX(position.x);
        mount.setY(position.y);

        // Center body horizontally on mount
        float bodyX;
        if(direction == 0){
            bodyX = position.x + (
                mount.getRegion().getRegionWidth() * GUN_TURRET_SCALE
                    - body.getRegion().getRegionWidth() * GUN_TURRET_SCALE) / 2f;
        }else{
            bodyX = position.x + (
                     body.getRegion().getRegionWidth() * GUN_TURRET_SCALE / 4f);
        }
        float bodyY = position.y + (
            mount.getRegion().getRegionHeight() * GUN_TURRET_SCALE
                - body.getRegion().getRegionHeight() * GUN_TURRET_SCALE) / 2f;
        body.setX(bodyX);
        body.setY(bodyY);

        // Center gun on body
        float gunX = (direction == 0)
            ? bodyX + (body.getRegion().getRegionWidth() * GUN_TURRET_SCALE) / 2f - 4
            : bodyX - (body.getRegion().getRegionWidth() * GUN_TURRET_SCALE) / 2f;
        float gunY = bodyY + (body.getRegion().getRegionHeight() * GUN_TURRET_SCALE
            - gun.getRegion().getRegionHeight() * GUN_TURRET_SCALE) / 2f - 7;
        gun.setX(gunX);
        gun.setY(gunY);
    }

    private TextureRegion flipHorizontalTextureRegion(TextureRegion region){
        TextureRegion copy = new TextureRegion(region);
        copy.flip(true, false);
        return copy;
    }

}
