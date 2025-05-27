package com.svalero.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ShowRectangleDebug {

    private static final ShapeRenderer shapeRenderer = new ShapeRenderer();


    public static void inRed(float x, float y, float width, float height){
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
        shapeRenderer.dispose();
    }

    public static void drawRedLine(float x1, float y1, float x2, float y2) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(x1, y1, x2, y2);
        shapeRenderer.end();
    }
}
