package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        ranger.updateProjectiles(dt);
        ranger.update(dt);
        enemyManager.update(dt, ranger.getPosition());
        if(!ranger.isImmune() && !ranger.isDestroyed()){
            checkCollisions();
        }
        for(Explosion explosion: explosions){
            explosion.update(dt);
        }
    }

    public void checkCollisions() {
        boolean collision = false;
        int index = 0;
        while(!collision && enemyManager.getEnemies().size() > index){
            Character enemy = enemyManager.getEnemies().get(index);
            Rectangle hitBox = enemy.getHitBox();
            if(hitBox.overlaps(ranger.getHitBox())) {
                ranger.lostLife(RANGER_IMMUNITY_DURATION);
                if (!ranger.isDestroyed()) {
                    ranger.setImmune(true);
                } else {
                    explosions.add(new Explosion(ranger.getPosition(), CHARACTER_TYPE.RANGER));
                    ranger.dispose();
                    //TODO termina partida
                }
                Vector2 position = enemy.getPosition();
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
