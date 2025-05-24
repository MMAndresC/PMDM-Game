package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.game.MyGame;
import com.svalero.game.characters.*;
import com.svalero.game.characters.Character;
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

    private List<Explosion> explosions;


    public LogicManager(MyGame game) {
        this.game = game;
        this.ranger = new Ranger();
        this.enemyManager = new EnemyManager();
        this.explosions = new ArrayList<>();
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
    }

    public void update(float dt) {
        handleInput(dt);
        ranger.update(dt);
        enemyManager.update(dt, ranger.getPosition());
        if(!ranger.isImmune() && !ranger.isDestroyed()){
            checkBodyCollisions();
            checkEnemiesProjectilesCollisions();
            checkRangerProjectilesCollision();
        }
        for(Explosion explosion: explosions){
            explosion.update(dt);
        }
    }

    public void checkRangerProjectilesCollision(){
        if(ranger.isDestroyed()) return;
        for(Projectile projectile: ranger.getProjectiles()){
            if(projectile.isDestroyed()) continue;
            for(Character enemy: enemyManager.getEnemies()){
                if(enemy.isActive() && projectile.getRect() != null && projectile.getRect().overlaps(enemy.getHitBox())){
                    projectile.setStatus(STATUS.DESTROYED);
                    enemy.hit(RANGER_BULLET_DAMAGE);
                    if(enemy.noHitPointsLeft()){
                        ranger.sumScore(enemy.getPointsScore());
                        enemy.setStatus(STATUS.DESTROYED);
                        if(enemy instanceof GunTurret){
                            if(!((GunTurret) enemy).getMissiles().isEmpty()){
                                enemy.setStatus(STATUS.INACTIVE);
                            }
                        }
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

    public void checkEnemiesProjectilesCollisions(){
        if(ranger.isDestroyed()) return;
        boolean collision = false;
        int index = 0;
        while(!collision && enemyManager.getProjectiles().size() > index){
            Projectile projectile = enemyManager.getProjectiles().get(index);
            Rectangle rect = projectile.getRect();
            if(rect != null && rect.overlaps(ranger.getHitBox())) {
                collision = true;
                ranger.hit(projectile.getDamage());
                if (ranger.isDestroyed()) {
                    Vector2 position = getCenterPositionExplosion(ranger.getHitBox());
                    explosions.add(new Explosion(position, CHARACTER_TYPE.RANGER));
                    ranger.dispose();
                    //TODO termina partida
                }
            }else index++;
        }
        if(collision){
            //EnemyManager has a projectiles copy, remove in copy and in the original
            //GunTurret & FighterSquadron, that is reason why new projectiles are created sending
            //shooter parent. Projectile and shooter are related in both ways
            Projectile p = enemyManager.getProjectiles().get(index);
            Character shooter = null;

            if (p instanceof Missile missile) {
                shooter = missile.getShooter();
                if (shooter instanceof GunTurret turret) {
                    turret.removeMissile(missile);
                }
            }else if(p.getOriginSquadron() != null){
                p.getOriginSquadron().getProjectiles().remove(p);
            }
            p.dispose();
            enemyManager.getProjectiles().get(index).dispose();
            enemyManager.getProjectiles().remove(index);
        }
    }

    public void checkBodyCollisions() {
        if(ranger.isDestroyed()) return;
        boolean collision = false;
        int index = 0;
        while(!collision && enemyManager.getEnemies().size() > index){
            Character enemy = enemyManager.getEnemies().get(index);
            Rectangle hitBox = enemy.getHitBox();
            if(hitBox != null && hitBox.overlaps(ranger.getHitBox())) {
                ranger.lostLife(RANGER_IMMUNITY_DURATION);
                if (ranger.isDestroyed()) {
                    Vector2 position = getCenterPositionExplosion(enemy.getHitBox());
                    explosions.add(new Explosion(position, CHARACTER_TYPE.RANGER));
                    ranger.dispose();
                    //TODO termina partida
                }

                Vector2 position = getCenterPositionExplosion(enemy.getHitBox());
                CHARACTER_TYPE type = enemy.getType();

                if (enemy instanceof Fighter fighter) {
                    fighter.setStatus(STATUS.DESTROYED);
                    fighter.dispose();
                }if(enemy instanceof GunTurret gunTurret) {
                    if(!gunTurret.getMissiles().isEmpty()) gunTurret.setStatus(STATUS.INACTIVE);
                    else {
                        gunTurret.setStatus(STATUS.DESTROYED);
                        gunTurret.dispose();
                        enemyManager.getEnemies().remove(index);
                    }
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
}
