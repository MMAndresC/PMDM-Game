package com.svalero.game.projectiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.managers.R;
import com.svalero.game.utils.DrawInfoEffect;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Ray extends Projectile {

    private RAY_STATE state;
    private final Vector2 origin;
    private final Vector2 target;
    private final Vector2 direction;
    private float currentLength;
    private float holdTimer;
    private final float maxLength;
    private final DrawInfoEffect drawInfo;
    private Polygon polygon; //Rect not work with angles

    public Ray(Vector2 origin, float speed, float damage, Vector2 rangerPosition) {
        super(origin, speed, damage);
        this.origin = origin.cpy();
        this.target = rangerPosition.cpy();
        this.direction = new Vector2(target).sub(origin).nor();
        this.maxLength = origin.dst(target);
        this.currentLength = 0;
        this.state = RAY_STATE.GROWING;
        this.holdTimer = 0;
        this.rect = new Rectangle();

        // Load textures
        Array<TextureRegion> frames = R.getEnemiesRegions(FRIGATE_RAY);
        animation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        TextureRegion frame = animation.getKeyFrame(0);

        float angle = direction.angleDeg();
        this.drawInfo = new DrawInfoEffect(
            frame,
            origin.x,
            origin.y,
            frame.getRegionWidth() / 2f,
            0,
            frame.getRegionWidth(),
            0, // Start length 0
            1f,
            1f,
            angle - 90 // Adjust rotation
        );
    }

    @Override
    public void update(float dt) {
        animationTime += dt;
        drawInfo.setRegion(animation.getKeyFrame(animationTime));

        switch (state) {
            case GROWING:
                // Extend ray to target coordinates
                currentLength += speed * dt;

                if (currentLength >= maxLength) {
                    state = RAY_STATE.HOLDING;
                }

                drawInfo.setHeight(currentLength);
                break;

            case HOLDING:
                holdTimer += dt;
                if (holdTimer >= RAY_HOLD_DURATION) {
                    status = STATUS.DESTROYED;
                }
                break;
        }

        float halfWidth = drawInfo.getWidth() / 2f;
        float[] vertices = {
            -halfWidth, 0,
            halfWidth, 0,
            halfWidth, currentLength,
            -halfWidth, currentLength
        };
        polygon = new Polygon(vertices);
        polygon.setOrigin(0, 0);
        polygon.setPosition(origin.x, origin.y);
        polygon.setRotation(direction.angleDeg() - 90);
    }
}
