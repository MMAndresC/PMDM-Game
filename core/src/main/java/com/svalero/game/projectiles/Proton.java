package com.svalero.game.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.constants.Constants;
import com.svalero.game.managers.R;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Proton extends Projectile {

    private Vector2 direction;
    private Vector2 velocity;

    public Proton(Vector2 position, float speed, float damage, Vector2 direction) {
        super(position, speed, damage);
        this.direction = direction;
        animationTime = 0;
        status = STATUS.ACTIVE;
        velocity = direction.cpy().scl(speed);
        scale = DREADNOUGHT_PROTON_SCALE;

        //Load textures
        Array<TextureRegion> frames = R.getEnemiesRegions(DREADNOUGHT_PROTON);
        animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        currentFrame = animation.getKeyFrame(0);

        rect = new Rectangle();
    }

    @Override
    public void update(float dt) {
        animationTime += dt;
        currentFrame = animation.getKeyFrame(animationTime);
        position.mulAdd(velocity, dt);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        //Change status of proton out of screen
        if (position.x + currentFrame.getRegionWidth() < 0 || position.x > screenWidth ||
            position.y + currentFrame.getRegionHeight() < 0 || position.y > screenHeight) {
            status = Constants.STATUS.OUT;
            return;
        }

        rect = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }
}
