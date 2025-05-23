package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.MyGame;
import com.svalero.game.characters.Character;
import com.svalero.game.characters.Explosion;
import com.svalero.game.characters.Fighter;
import com.svalero.game.characters.ProjectileRanger;
import com.svalero.game.characters.Ranger;
import com.svalero.game.constants.Constants;
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
    private FighterSquadronManager fighterSquadronManager;

    private List<Explosion> explosions;


    public LogicManager(MyGame game) {
        this.game = game;
        this.ranger = new Ranger();
        this.enemyManager = new EnemyManager();
        this.fighterSquadronManager = new FighterSquadronManager();
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
        this.ranger.setMoving(isMoving);

        //Shoot
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            //Spacing out shots
            float currentTime = TimeUtils.nanoTime() / 1_000_000_000f; // Seconds
            if (currentTime - ranger.getLastShot() >= ranger.getFireRate()) {
                ranger.setLastShot(currentTime);
                createProjectile();
            }
        }

        //Control screen limits so that ranger does not go out of the screen
        float minX = this.ranger.getRangerWidth() / 2f;
        float maxX = Gdx.graphics.getWidth() - this.ranger.getRangerWidth() / 2f;

        float minY = 0;
        float maxY = Gdx.graphics.getHeight() - this.ranger.getRangerHeight();

        Vector2 position = new Vector2(
            Math.max(minX, Math.min(x, maxX)),
            Math.max(minY, Math.min(y, maxY))
        );
        this.ranger.setPosition(position);
    }

    private void createProjectile(){
        Vector2 position = new Vector2(
            ranger.getPosition().x - 2f,
            ranger.getPosition().y + (ranger.getRangerHeight() / 2)
        );
        ranger.getProjectiles().add(
            new ProjectileRanger(
                position, ranger.getBulletSpeed(), ranger.getFireRate(), ranger.getBulletDamage()
            )
        );
    }

    public void update(float dt) {
        ranger.updateProjectiles(dt);
        handleInput(dt);
        ranger.update(dt);
        enemyManager.update(dt, ranger.getPosition());
        fighterSquadronManager.update(dt, ranger.getPosition());
        if(!ranger.isImmune()){
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
            if(hitBox.overlaps(ranger.getHitBox())){
                ranger.lostLife();
                if(!ranger.isDestroyed()){
                    ranger.setImmune(true);
                }else{
                    explosions.add(new Explosion(ranger.getPosition(), CHARACTER_TYPE.RANGER));
                    ranger.setPosition(new Vector2(-200, 0)); //Out of screen
                    //TODO termina partida
                }
                Vector2 position = enemy.getPosition();
                CHARACTER_TYPE type = enemy.getType();

                if (enemy instanceof Fighter fighter) {
                    fighter.setStatus(STATUS.DESTROYED);
                } else {
                    enemyManager.getEnemies().remove(index);
                }

                explosions.add(new Explosion(position, type));
                collision = true;
            }else
                index++;
        }
    }
}
