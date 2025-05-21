package com.svalero.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.svalero.game.MyGame;
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

    private float animationTime;

    public LogicManager(MyGame game) {
        this.game = game;
        this.ranger = new Ranger();
        this.animationTime = 0f;
    }

    private void handleInput(float dt) {
        boolean isMoving = false;
        float x = ranger.getPosition().x;
        float y = ranger.getPosition().y;

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

    public void update(float dt, float animationTime) {
        this.ranger.setAnimationTime(animationTime);
        handleInput(dt);
    }
}
