package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.game.characters.*;

import com.svalero.game.characters.Character;
import com.svalero.game.projectiles.Projectile;
import com.svalero.game.levels.LevelEnemies;
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
    private FighterSquadronManager fighterSquadronManager;

    private float timeSinceLastSpawn;

    private List<LevelEnemies> levelEnemies;

    private int indexEnemy;

    private Vector2 rangerPosition;

    public EnemyManager() {
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();
        timeSinceLastSpawn = 0;
        indexEnemy = 0;
        rangerPosition = new Vector2();
        fighterSquadronManager = new FighterSquadronManager();
        levelEnemies = new ArrayList<>();
    }

    public void update(float dt, Vector2 rangerPosition) {
        timeSinceLastSpawn += dt;
        this.rangerPosition.set(rangerPosition);

        //Create new enemy
        if (indexEnemy < levelEnemies.size() && timeSinceLastSpawn >= levelEnemies.get(indexEnemy).getDelay()) {
            timeSinceLastSpawn = 0;
            generateEnemy();
        }

        // Update all enemies
        for (Character enemy : enemies) {
            if(enemy instanceof Kamikaze)
                enemy.update(dt, rangerPosition);
            else
                enemy.update(dt);
        }
        // Update fighter squadrons and add their fighters to the general list
        fighterSquadronManager.update(dt, rangerPosition);
        enemies.removeIf(enemy -> enemy instanceof Fighter); // Remove fighter references
        enemies.addAll(fighterSquadronManager.getAllFighters()); //Load new with updates to synchronize
        //Update projectiles
        for(Projectile projectile : projectiles) {
            projectile.update(dt);
        }
        // Check if enemies have to create projectiles
        for(Character enemy: enemies){
            if(enemy instanceof GunTurret){
                Projectile missile = ((GunTurret) enemy).createMissile(rangerPosition);
                if(missile != null)
                    projectiles.add(missile);
            }else if(enemy instanceof Fighter){
                List<Projectile> beams = ((Fighter) enemy).getSquadron().createProjectile();
                if(beams != null)
                    projectiles.addAll(beams);
            }else if(enemy instanceof Frigate) {
                Projectile ray = ((Frigate) enemy).createProjectile(rangerPosition);
                if(ray != null)
                    projectiles.add(ray);
            }
        }
        removeElementsOutScreen();
    }

    public void removeElementsOutScreen() {
        enemies.removeIf(enemy -> enemy.getStatus() == STATUS.OUT);
        projectiles.removeIf(projectile -> projectile.getStatus() == STATUS.OUT
            || projectile.getStatus() == STATUS.DESTROYED
        );
    }

    public void generateEnemy() {
        ENEMY_TYPE enemyType = levelEnemies.get(indexEnemy).getType();

        switch (enemyType) {
            case ASTEROID -> createAsteroidShower();
            case FIGHTER_SQUADRON -> createFightersSquadron();
            case GUN_TURRET -> createGunTurret();
            case KAMIKAZE -> createKamikaze();
            case FRIGATE -> createFrigate();
        }

        indexEnemy++;
    }

    public void createFrigate(){
        Frigate frigate = new Frigate();
        enemies.add(frigate);
    }

    public void createKamikaze(){
        Kamikaze kamikaze = new Kamikaze(rangerPosition);
        enemies.add(kamikaze);
    }

    public void createGunTurret(){
        GunTurret gunTurret = new GunTurret();
        enemies.add(gunTurret);
    }

    public void createFightersSquadron() {
        fighterSquadronManager.createSquadron();
        enemies.addAll(fighterSquadronManager.getAllFighters());
    }

    public void createAsteroidShower() {
        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();
        //Generate random origin left or right
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

            //Spacing to avoid overlap, random to change origin position
            float spacing = asteroidWidth + 20;

            float offset = MathUtils.random(-offsetRange, offsetRange);
            float startX = baseX + i * (spacing + offset) * (direction == 0 ? 1 : -1);
            float startY = MathUtils.random(screenHeight * 2 / 3f, screenHeight);

            asteroid.setPosition(new Vector2(startX, startY));
            asteroid.setHitBox(new Rectangle(startX, startY, asteroidWidth, asteroidHeight));

            enemies.add(asteroid);
        }
    }

    public void clear(){
        projectiles.clear();
        enemies.clear();
        indexEnemy = 0;
        levelEnemies.clear();
        timeSinceLastSpawn = 0;
        fighterSquadronManager.clear();
    }
}
