package com.svalero.game.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player extends Character{

    private List<Projectile> projectiles;

    private int score;

    public Player(float x, float y, float speed) {
        super(x, y, speed);

        projectiles = new ArrayList<Projectile>();
        score = 0;

    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(currentFrame, getX(), getY());
    }
}
