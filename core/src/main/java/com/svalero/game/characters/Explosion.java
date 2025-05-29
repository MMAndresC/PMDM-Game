package com.svalero.game.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.game.managers.R;
import com.svalero.game.utils.DrawInfo;
import lombok.Data;

import static com.svalero.game.constants.Constants.*;

@Data
public class Explosion {

    private Animation<TextureRegion> animation;
    private float animationTime;
    private DrawInfo explosion;
    private Vector2 position;


    public Explosion(Vector2 position, CHARACTER_TYPE type, float scale, float frameDuration) {
        this.position = new Vector2(position);
        animationTime = 0;
        Array<TextureRegion> frames = switch (type) {
            case RANGER -> R.getRangerRegions(RANGER_EXPLOSION);
            case KAMIKAZE -> R.getEnemiesRegions(KAMIKAZE_EXPLOSION);
            case ASTEROID -> R.getEnemiesRegions(ASTEROID_EXPLOSION);
            case FRIGATE -> R.getEnemiesRegions(FRIGATE_EXPLOSION);
            case DREADNOUGHT -> R.getEnemiesRegions(DREADNOUGHT_EXPLOSION);
            default -> R.getEnemiesRegions(DEFAULT_ENEMY_EXPLOSION);
        };
        animation = new Animation<>(frameDuration, frames, Animation.PlayMode.NORMAL);
        TextureRegion texture = animation.getKeyFrame(animationTime);
            explosion = new DrawInfo(texture, position.x, position.y,
                texture.getRegionWidth() * scale,
                texture.getRegionHeight() * scale);
    }

    public void update(float dt) {
        animationTime += dt;
        TextureRegion texture = animation.getKeyFrame(animationTime);
        explosion.setRegion(texture);
        explosion.setX(position.x - texture.getRegionWidth() / 2f);
        explosion.setY(position.y - texture.getRegionHeight() / 2f);
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(animationTime);
    }

}
