package joc1;

import java.lang.Math;

public class Vec2 {
    float x;
    float y;

    public Vec2() {
        x = 0;
        y = 0;
    };

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    Vec2 add(Vec2 vec) {
        return new Vec2(x + vec.x, y + vec.y);
    }

    void Add(Vec2 vec) {
        x += vec.x;
        y += vec.y;
    }

    Vec2 scale(float s) {
        return new Vec2(x * s, y * s);
    }

    public float norm() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public Vec2 normalized() {
        float norm = norm();
        if (norm == 0) return new Vec2();
        return new Vec2(x/norm(), y/norm());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
