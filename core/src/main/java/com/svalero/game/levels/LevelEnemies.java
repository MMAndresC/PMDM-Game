package com.svalero.game.levels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.svalero.game.constants.Constants.ENEMY_TYPE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelEnemies {

    private ENEMY_TYPE type;
    private float delay;
}
