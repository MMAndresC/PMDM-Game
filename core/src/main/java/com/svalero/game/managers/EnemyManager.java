package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.game.characters.Asteroid;
import com.svalero.game.characters.Character;
import com.svalero.game.characters.Projectile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.svalero.game.constants.Constants.*;

@Data
@AllArgsConstructor
public class EnemyManager {

    private List<Character> enemies;
    private List<Projectile> projectiles;

    private float timeSinceLastSpawn;

    private List<ENEMY_TYPE> level1 = List.of(ENEMY_TYPE.ASTEROID, ENEMY_TYPE.ASTEROID, ENEMY_TYPE.ASTEROID);
    private int indexEnemy;

    public EnemyManager(){
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();
        timeSinceLastSpawn = 0;
        indexEnemy = 0;
    }

    public void update(float dt){
        timeSinceLastSpawn += dt;
        if(timeSinceLastSpawn >= ENEMY_SPAWN_DELAY && indexEnemy < level1.size()){
            timeSinceLastSpawn = 0;
            generateEnemy();
        }
        for(Character enemy: enemies){
            enemy.update(dt);
        }
       removeElementsOutScreen();
    }

    public void removeElementsOutScreen(){
        //Remove enemies out of screen
        enemies.removeIf(enemy -> enemy.getStatus() == STATUS.OUT);

    }

    public void generateEnemy(){
        ENEMY_TYPE enemyType = level1.get(indexEnemy);

        switch(enemyType){
            case ASTEROID:
                createAsteroidShower();
        }

        indexEnemy++;
    }

    public void createAsteroidShower() {
        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();
        int direction = MathUtils.random(0, 1);

        float offsetRange = 30f;

        for (int i = 0; i < ASTEROID_SHOWER; i++) {
            Asteroid asteroid = new Asteroid();
            asteroid.setDirection(direction);
            float asteroidWidth = asteroid.getCurrentFrame().getRegionWidth();
            float asteroidHeight = asteroid.getCurrentFrame().getRegionHeight();
            float baseX = (direction == 0)
                ? -asteroidWidth
                : screenWidth;

            float spacing = asteroid.getCurrentFrame().getRegionWidth() + 20; //Avoid superposition

            float offset = MathUtils.random(-offsetRange, offsetRange);
            float startX = baseX + i * (spacing + offset) * (direction == 0 ? 1 : -1);

            float startY = MathUtils.random(screenHeight * 2 / 3f, screenHeight);

            asteroid.setPosition(new Vector2(startX, startY));
            //Set collision rectangle
            asteroid.setHitBox(new Rectangle(startX, startY, asteroidWidth, asteroidHeight));
            enemies.add(asteroid);
        }
    }

}
