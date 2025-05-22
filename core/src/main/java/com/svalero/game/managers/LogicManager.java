package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.svalero.game.MyGame;
import com.svalero.game.characters.ProjectileRanger;
import com.svalero.game.characters.Ranger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.svalero.game.constants.Constants.RANGER_SPEED;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogicManager {

    private MyGame game;

    private Ranger ranger;

    private EnemyManager enemyManager;


    public LogicManager(MyGame game) {
        this.game = game;
        this.ranger = new Ranger();
        this.enemyManager = new EnemyManager();
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
        enemyManager.update(dt);
    }
}
