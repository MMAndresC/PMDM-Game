package com.svalero.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.svalero.game.characters.Character;
import com.svalero.game.projectiles.Projectile;
import com.svalero.game.projectiles.Ray;

import java.util.List;

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

    public static void DEBUG_showRangerRectangle(Rectangle rect){
            ShowRectangleDebug.inRed(rect.x, rect.y, rect.width,rect.height);
    }

    public static void DEBUG_showEnemiesRectangle(List<com.svalero.game.characters.Character> enemies){
        for(Character enemy : enemies){
            ShowRectangleDebug.inRed(
                enemy.getHitBox().x,
                enemy.getHitBox().y,
                enemy.getHitBox().width,
                enemy.getHitBox().height
            );
        }
    }

    public static void DEBUG_showRaysPolygon(List<Projectile> projectiles){
        List<Projectile> filtered = projectiles.stream()
            .filter(p -> p instanceof Ray)
            .toList();

        for(Projectile p : filtered){
            Ray ray = (Ray) p;
            Polygon poly = ray.getPolygon();
            if (poly != null) {
                float[] vertices = poly.getTransformedVertices();
                for (int i = 0; i < vertices.length; i += 2) {
                    float x1 = vertices[i];
                    float y1 = vertices[i + 1];
                    float x2 = vertices[(i + 2) % vertices.length];
                    float y2 = vertices[(i + 3) % vertices.length];
                    ShowRectangleDebug.drawRedLine(x1, y1, x2, y2);
                }
            }
        }
    }

    public static void DEBUG_showProjectilesRectangle(List<Projectile> projectiles){
        List<Projectile> filtered = projectiles.stream()
            .filter(p -> !(p instanceof Ray))
            .toList();

        for(Projectile p : filtered){
            ShowRectangleDebug.inRed(
                p.getRect().x,
                p.getRect().y,
                p.getRect().width,
                p.getRect().height
            );
        }
    }
}
