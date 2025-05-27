package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.MyGame;
import com.svalero.game.characters.*;
import com.svalero.game.characters.Character;
import com.svalero.game.items.PowerUp;
import com.svalero.game.projectiles.Projectile;
import com.svalero.game.projectiles.Ray;
import com.svalero.game.screen.GameOverScreen;
import com.svalero.game.screen.GameScreen;
import com.svalero.game.screen.PauseScreen;
import com.svalero.game.levels.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.svalero.game.constants.Constants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogicManager {

    private MyGame game;

    private Ranger ranger;

    private EnemyManager enemyManager;

    private LevelManager levelManager;

    private PowerUpManager powerUpManager;

    private List<Explosion> explosions;

    private int numberLevel;

    private String background;

    private boolean isLevelOver;

    private float levelCompleteTimer;

    private GameScreen gameScreen;

    private float freezeTime;

    private boolean isPaused;


    public LogicManager(MyGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.ranger = new Ranger();
        this.enemyManager = new EnemyManager();
        this.levelManager = new LevelManager();
        this.powerUpManager = new PowerUpManager();
        this.explosions = new ArrayList<>();
        this.numberLevel = 1;
        this.isLevelOver = false;
        this.levelCompleteTimer = 0;
        this.freezeTime = 0;
        this.isPaused = false;
        initializeLevel();
    }

    public void initializeLevel(){
        Level level = levelManager.getCurrentLevel(numberLevel);
        System.out.println("Level number: " + numberLevel + " " + level);
        if(level == null){
            if(numberLevel == 1) {
                Gdx.app.error("LevelManager", "Error al leer nivel: " + numberLevel);
                Gdx.app.exit();
            }
            game.setScreen(new GameOverScreen(game, ranger.getScore()));
            return;
        }
        isLevelOver = false;
        levelCompleteTimer = 0;
        background = level.getBackground();
        //Initialize managers
        enemyManager.clear();
        enemyManager.setLevelEnemies(level.getEnemies());
        powerUpManager.clear();
        powerUpManager.setLevelPowerUps(level.getPowerUps());
    }

    public void checkLevelEnd(float dt){
        // All enemies destroyed or out of screen
        if(isLevelOver) levelCompleteTimer += dt;
        if(enemyManager.getEnemies().isEmpty()
            && enemyManager.getIndexEnemy() >= enemyManager.getLevelEnemies().size()){
            isLevelOver = true;
            if(levelCompleteTimer >= LEVEL_DELAY){
                //Reset enemyManager
                enemyManager = new EnemyManager();
                numberLevel++;
                //Add bonus to score
                float score = ranger.getScore();
                float sumBonus = ranger.getLives() * SCORE_BONUS_LIVES;
                ranger.setScore(score + sumBonus);
                initializeLevel();
            }
        }
    }

    private void handleInput(float dt) {
        boolean isMoving = false;
        float x = ranger.getPosition().x;
        float y = ranger.getPosition().y;

        //Movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= RANGER_SPEED * dt;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += RANGER_SPEED * dt;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += RANGER_SPEED * dt;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= RANGER_SPEED * dt;
            isMoving = true;
        }

        ranger.setNewPosition(x, y, isMoving);

        //Shoot
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            ranger.createProjectile();
        }

        //Pause
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            freezeTime = TimeUtils.nanoTime() / 1_000_000_000f;
            isPaused = true;
            game.setScreen(new PauseScreen(game, gameScreen));
        }
    }

    public void update(float dt) {
        handleInput(dt);
        ranger.update(dt);
        enemyManager.update(dt, ranger.getPosition());
        powerUpManager.update(dt);
        if(!ranger.isImmune() && !ranger.isDestroyed()){
            checkBodyCollisions();
            checkEnemiesProjectilesCollisions();
            checkRangerProjectilesCollision();
        }
        if(!ranger.isDestroyed()) //Immune ranger can catch items
            checkRangerPowerUpsCollision();
        for(Explosion explosion: explosions){
            explosion.update(dt);
        }
        checkLevelEnd(dt);
    }

    public void checkRangerPowerUpsCollision(){
        if(ranger.isDestroyed() || isLevelOver) return;
        //Increase a little ranger rect,
        Rectangle rangerRect = ranger.setLenientHitBox();
        for(PowerUp powerUp: powerUpManager.getPowerUps()){
            if(powerUp.isActive() && powerUp.getRect().overlaps(rangerRect)){
                powerUp.setStatus(STATUS.DESTROYED);
                ranger.setPowerUp(powerUp.getType());
            }
        }
        powerUpManager.getPowerUps().removeIf(powerUp -> powerUp.getStatus() == STATUS.DESTROYED);
    }

    public void checkRangerProjectilesCollision(){
        if(ranger.isDestroyed() || isLevelOver) return;

        for(Projectile projectile: ranger.getProjectiles()){
            if(projectile.isDestroyed()) continue;

            for(Character enemy: enemyManager.getEnemies()){
                if(enemy.isActive() && projectile.getRect() != null && projectile.getRect().overlaps(enemy.getHitBox())){
                    projectile.setStatus(STATUS.DESTROYED);
                    enemy.hit(RANGER_BULLET_DAMAGE);
                    if(enemy.noHitPointsLeft()){
                        ranger.sumScore(enemy.getPointsScore());
                        enemy.setStatus(STATUS.DESTROYED);
                        CHARACTER_TYPE type = returnType(enemy);
                        Vector2 position = getCenterPositionExplosion(enemy.getHitBox());
                        explosions.add(new Explosion(position, type));
                        enemy.dispose();
                    }
                }
            }
        }
        ranger.getProjectiles().removeIf(Projectile::isDestroyed);
        enemyManager.getEnemies().removeIf(Character::isDestroyed);
    }

    public Vector2 getCenterPositionExplosion(Rectangle rect){
        float centerX = rect.x + rect.width / 2f;
        float centerY = rect.y + rect.height / 2f;
        return new Vector2(centerX, centerY);
    }

    public CHARACTER_TYPE returnType(Character enemy){
        if(enemy instanceof Fighter) return CHARACTER_TYPE.FIGHTER;
        if(enemy instanceof Kamikaze) return CHARACTER_TYPE.KAMIKAZE;
        if(enemy instanceof GunTurret) return CHARACTER_TYPE.GUN_TURRET;
        if(enemy instanceof Asteroid) return CHARACTER_TYPE.ASTEROID;
        return CHARACTER_TYPE.FIGHTER;
    }

    public void checkEnemiesProjectilesCollisions() {
        if (ranger.isDestroyed() || isLevelOver) return;

        boolean collision = false;
        int index = 0;

        while (!collision && enemyManager.getProjectiles().size() > index) {
            Projectile projectile = enemyManager.getProjectiles().get(index);

            if (projectile instanceof Ray) {
                if (overlapProjectilePolygon((Ray) projectile)) {
                    collision = true;
                } else index++;
            } else {
                Rectangle rect = projectile.getRect();
                if (rect != null && rect.overlaps(ranger.getHitBox())) {
                    projectile.setStatus(STATUS.DESTROYED);
                    collision = true;
                } else index++;
            }
        }

        if (collision) {
            Projectile projectile = enemyManager.getProjectiles().get(index);

            if (!(projectile instanceof Ray)) {
                projectile.dispose();
                enemyManager.getProjectiles().remove(index);
            }

            if (isRangerDestroyed(projectile)) {
                ranger.dispose();
                game.setScreen(new GameOverScreen(game, ranger.getScore()));
            }
        }
    }

    public boolean overlapProjectilePolygon(Ray projectile){
        Polygon polygon = projectile.getPolygon();
        Rectangle hitBox = ranger.getHitBox();
        Polygon rPoly = new Polygon(new float[] {
            0, 0,
            hitBox.width, 0,
            hitBox.width, hitBox.height,
            0, hitBox.height
        });
        rPoly.setPosition(hitBox.x, hitBox.y);
        if(polygon != null && Intersector.overlapConvexPolygons(polygon, rPoly)) {
            return isRangerDestroyed(projectile);
        }
        return false;
    }

    public boolean isRangerDestroyed(Projectile projectile){
        if(ranger.isShieldActive())
            ranger.hitShield(projectile.getDamage());
        else
            ranger.hit(projectile.getDamage());

        if (ranger.isDestroyed()) {
            Vector2 position = getCenterPositionExplosion(ranger.getHitBox());
            explosions.add(new Explosion(position, CHARACTER_TYPE.RANGER));
            return true;
        }
        return false;
    }

    public void checkBodyCollisions() {
        if(ranger.isDestroyed() || isLevelOver) return;
        boolean collision = false;
        int index = 0;
        while(!collision && enemyManager.getEnemies().size() > index){
            Character enemy = enemyManager.getEnemies().get(index);
            Rectangle hitBox = enemy.getHitBox();
            if(hitBox != null && hitBox.overlaps(ranger.getHitBox())) {
                if(ranger.isShieldActive()){
                    ranger.destroyShield();
                }else{
                    ranger.lostLife(RANGER_IMMUNITY_DURATION);
                    if (ranger.isDestroyed()) {
                        Vector2 position = getCenterPositionExplosion(enemy.getHitBox());
                        explosions.add(new Explosion(position, CHARACTER_TYPE.RANGER));
                        ranger.dispose();
                        game.setScreen(new GameOverScreen(game, ranger.getScore()));
                        return;
                    }
                }

                Vector2 position = getCenterPositionExplosion(enemy.getHitBox());
                CHARACTER_TYPE type = enemy.getType();

                if (enemy instanceof Fighter fighter) {
                    fighter.setStatus(STATUS.DESTROYED);
                    fighter.dispose();
                }else {
                    enemyManager.getEnemies().get(index).dispose();
                    enemyManager.getEnemies().remove(index);
                }

                explosions.add(new Explosion(position, type));
                collision = true;
            }else
                index++;
        }
    }

    public void adjustFreezeTime(){
        float now = TimeUtils.nanoTime() / 1_000_000_000f;
        float pausedDuration = now - freezeTime;
        ranger.setLastShot(ranger.getLastShot() + pausedDuration);
        for(Character enemy: enemyManager.getEnemies()){
            enemy.setLastShot(enemy.getLastShot() + pausedDuration);
        }
        freezeTime = 0;
    }
}
