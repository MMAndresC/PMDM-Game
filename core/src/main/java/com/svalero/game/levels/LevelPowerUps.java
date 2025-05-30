package com.svalero.game.levels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.svalero.game.constants.Constants.POWER_UP;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelPowerUps {

    private float delay;
    private POWER_UP type;
}
