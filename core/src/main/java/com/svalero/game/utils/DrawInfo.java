package com.svalero.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawInfo {

    TextureRegion region;
    float x = 0;
    float y = 0;
    float width = 0;
    float height = 0;

    public void update(TextureRegion region, float x, float y, float width, float height) {
        this.region = region;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
