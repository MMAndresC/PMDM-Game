package com.svalero.game.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelAnimations {

    private AnimationInfo animationInfo;
    private float x;
    private float y;
    private float scale;
    private float width;
    private float height;
    private float duration;
}
