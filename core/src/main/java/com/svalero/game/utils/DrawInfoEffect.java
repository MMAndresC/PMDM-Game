package com.svalero.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawInfoEffect extends DrawInfo{

    private float originX, originY;
    private float scaleX, scaleY;
    private float rotation;

    public DrawInfoEffect(
        TextureRegion region, float x, float y, float originX, float originY,
        float width, float height, float scaleX, float scaleY, float rotation
    ) {
        super(region, x, y, width, height);
        this.originX = originX;
        this.originY = originY;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
    }
}
