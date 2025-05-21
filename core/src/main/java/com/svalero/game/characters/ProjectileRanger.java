package com.svalero.game.characters;

import com.badlogic.gdx.math.Vector2;
import com.svalero.game.managers.R;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProjectileRanger extends Projectile{

    public ProjectileRanger(Vector2 position, float speed, float fireRate, float damage) {
        super(position, speed, fireRate, damage);

        texture = R.getRangerTexture(RANGER_AMMO_MINI_GUN);
    }

    @Override
    public void update(float dt) {
        position.y += speed * dt;
    }
}
