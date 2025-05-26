package com.svalero.game.projectiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.game.constants.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BeamDefault extends Projectile{

    public BeamDefault(Vector2 position, float speed, float damage, TextureRegion currentFrame){
        super(position, speed, damage, currentFrame);
    }

    @Override
    public void update(float dt) {
        position.y -= speed * dt;
        if(position.y <= -currentFrame.getRegionHeight()){
            status = Constants.STATUS.OUT;
            return;
        }
        rect = new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }
}
