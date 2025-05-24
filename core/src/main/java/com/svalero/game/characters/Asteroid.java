package com.svalero.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.managers.R;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.svalero.game.constants.Constants.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Asteroid extends Character {

    private int direction; //0 left, 1 right

    public Asteroid() {
        //Init
        hitPoints = ASTEROID_HIT_POINTS;
        type = CHARACTER_TYPE.ASTEROID;
        status = STATUS.ACTIVE;

        //Load textures
        Array<TextureRegion> frames = R.getEnemiesRegions(ASTEROID);
        animation = new Animation<>(0.2f, frames, Animation.PlayMode.LOOP);
        animationTime = 0f;
        currentFrame = animation.getKeyFrame(animationTime);
        speed = ASTEROID_SPEED;

        //Init hit box
        hitBox = new Rectangle();
    }

    @Override
    public void update(float dt) {
        animationTime += dt;
        position.y -= BACKGROUND_SPEED * dt;
        if (direction == 0) {
            position.x += speed * dt; // left → right
        } else if (direction == 1) {
            position.x -= speed * dt; // right → left
        }

        //update hit box, keep width & height from initial frame
        hitBox = new Rectangle(position.x, position.y, hitBox.getWidth(), hitBox.getHeight());
        currentFrame = animation.getKeyFrame(animationTime);

        float screenWidth = Gdx.graphics.getWidth();

        status = STATUS.ACTIVE;
        if (position.x > screenWidth ||
            position.x + currentFrame.getRegionWidth() < 0 ||
            position.y + currentFrame.getRegionHeight() < 0) {
            status = STATUS.OUT;
        }
    }
}
