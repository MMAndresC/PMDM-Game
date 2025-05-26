package com.svalero.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.managers.R;
import com.svalero.game.utils.DrawInfoEffect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.svalero.game.constants.Constants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerUp {

    private Vector2 position;

    private POWER_UP type;

    private TextureRegion currentTexture;

    private float speed;

    private Rectangle rect;

    private STATUS status;

    public PowerUp(POWER_UP type, float speed) {
        this.type = type;
        this.speed = speed;
        rect = new Rectangle();
        position = new Vector2();
        status = STATUS.ACTIVE;
        switch (type) {
            case SHIELD -> currentTexture = R.getRangerTexture(SHIELD);
            case DAMAGE -> currentTexture = R.getRangerTexture(DAMAGE);
            case HEALTH -> currentTexture = R.getRangerTexture(HEALTH);
        }

        //Random coordinate x, y out of screen
        position.y = Gdx.graphics.getHeight();
        float max = Gdx.graphics.getWidth() - 50;
        float min = 50;
        position.x = MathUtils.random(min, max + 1);
    }

    public void update(float dt) {
        position.y -= speed * dt;
        if(position.y  < - currentTexture.getRegionHeight()){
            status = STATUS.OUT;
            rect = null;
            return;
        }
        rect.set(position.x, position.y, currentTexture.getRegionWidth() * POWER_UP_SCALE,
            currentTexture.getRegionHeight() * POWER_UP_SCALE);
    }

    public DrawInfoEffect drawWithRotationEffect() {
        float time = TimeUtils.nanoTime() / 1_000_000_000f;
        float rotation = (time * 90) % 360;

        float width = currentTexture.getRegionWidth();
        float height = currentTexture.getRegionHeight();
        float originX = width / 2f;
        float originY = height / 2f;

        return new DrawInfoEffect(
            currentTexture, position.x - originX, position.y - originY, originX, originY,
            width, height, POWER_UP_SCALE, POWER_UP_SCALE, rotation
        );
    }
}
