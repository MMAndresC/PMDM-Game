package com.svalero.game.characters;

import com.badlogic.gdx.graphics.Texture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Projectile extends Character{

    protected Texture texture;

    public abstract void update(float dt);
}
