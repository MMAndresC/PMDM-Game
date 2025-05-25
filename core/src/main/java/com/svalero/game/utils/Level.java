package com.svalero.game.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Level {

    private String background;
    private List<LevelAnimations> animations;
    private List<LevelEnemies> enemies;
    private List<LevelPowerUps> powerUps;
}
