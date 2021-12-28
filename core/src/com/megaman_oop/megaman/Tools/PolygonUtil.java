package com.megaman_oop.megaman.Tools;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class PolygonUtil {

    static final Vector2 center = new Vector2();
    static final Vector2 vec1 = new Vector2();
    static final Vector2 vec2 = new Vector2();

    public static boolean overlaps(Polygon polygon, Circle circle) {
        float[] vertices = polygon.getTransformedVertices();
        center.set(circle.x, circle.y);
        float squareRadius = circle.radius * circle.radius;
        for (int i = 0; i < vertices.length; i += 2) {
            if (i == 0) {
                if (Intersector.intersectSegmentCircle(vec1.set(vertices[vertices.length - 2], vertices[vertices.length - 1]),
                        vec2.set(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            } else {
                if (Intersector.intersectSegmentCircle(vec1.set(vertices[i - 2], vertices[i - 1]), vec2.set(vertices[i], vertices[i + 1]), center, squareRadius))
                    return true;
            }
        }
        return polygon.contains(circle.x, circle.y);
    }

}