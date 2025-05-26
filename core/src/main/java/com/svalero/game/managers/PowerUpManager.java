package com.svalero.game.managers;

import com.svalero.game.constants.Constants;
import com.svalero.game.items.PowerUp;
import com.svalero.game.levels.LevelPowerUps;
import lombok.Data;

import static com.svalero.game.constants.Constants.POWER_UP;
import static com.svalero.game.constants.Constants.POWER_UP_SPEED;

import java.util.ArrayList;
import java.util.List;

@Data
public class PowerUpManager {

    private float timeSinceLastSpawn;
    private int indexPowerUp;

    private List<LevelPowerUps> levelPowerUps;
    private List<PowerUp> powerUps;

    public PowerUpManager() {
        timeSinceLastSpawn = 0;
        indexPowerUp = 0;
        levelPowerUps = new ArrayList<>();
        powerUps = new ArrayList<>();
    }

    public void update(float dt) {
        timeSinceLastSpawn += dt;

        //Create new power up
        if (indexPowerUp < levelPowerUps.size() && timeSinceLastSpawn >= levelPowerUps.get(indexPowerUp).getDelay()) {
            timeSinceLastSpawn = 0;

            POWER_UP powerUpType = levelPowerUps.get(indexPowerUp).getType();
            powerUps.add(new PowerUp(powerUpType, POWER_UP_SPEED));

            indexPowerUp++;
        }

        //Update power ups
        for(PowerUp powerUp : powerUps){
            powerUp.update(dt);
        }

        //Remove power ups out of screen
        powerUps.removeIf(powerUp -> powerUp.getStatus() == Constants.STATUS.OUT);
    }

    public void clear(){
        timeSinceLastSpawn = 0;
        indexPowerUp = 0;
        levelPowerUps.clear();
        powerUps.clear();
    }
}
