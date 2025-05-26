package com.svalero.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.game.managers.R;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectileRanger extends Projectile{

    public ProjectileRanger(Vector2 position, float speed, float fireRate, float damage) {
        super(position, speed, fireRate, damage);

        currentFrame = R.getRangerTexture(RANGER_AMMO_MINI_GUN);
    }

    @Override
    public void update(float dt) {
        position.y += speed * dt;
        float screenHeight = Gdx.graphics.getHeight();
        if(position.y >= screenHeight){
            status = STATUS.OUT;
            return;
        }
        rect = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }
}
