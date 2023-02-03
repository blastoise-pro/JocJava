package joc1;

import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

public class Vec2 implements Cloneable{
    public float x;
    public float y;

    public Vec2() {
        x = 0;
        y = 0;
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 vec) {
        return new Vec2(x + vec.x, y + vec.y);
    }

    public void Add(Vec2 vec) {
        x += vec.x;
        y += vec.y;
    }

    public Vec2 sub(Vec2 vec) {
        return new Vec2(x - vec.x, y - vec.y);
    }

    public Vec2 scale(float s) {
        return new Vec2(x * s, y * s);
    }

    public void Scale(float s) {
        x *= s;
        y *= s;
    }

    public Vec2 cscale(Vec2 comps) {
        return new Vec2(x * comps.x, y * comps.y);
    }

    public void cScale(Vec2 comps) {
        x *= comps.x;
        y *= comps.y;
    }

    public float norm() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public float norm2() {
        return x*x + y*y;
    }

    public Vec2 normalized() {
        float norm = norm();
        if (norm == 0) return new Vec2();
        return new Vec2(x/norm, y/norm);
    }

    public float dot(Vec2 vec) {
        return x*vec.x + y*vec.y;
    }

    public float getAngle() {
        return (float) Math.atan2(y, x);
    }

    public Vec2 clamp(float min, float max) {
        float norm = norm();
        if (norm2() == 0)
            return new Vec2(x, y);
        if (norm > max) {
            return new Vec2(x * max/norm, y * max/norm);
        }
        else if (norm < min) {
            return new Vec2(x * max/norm, y * max/norm);
        }
        else {
            return new Vec2(x, y);
        }
    }

    public void Clamp(float min, float max) {
        float norm = norm();
        if (norm == 0)
            return;
        if (norm > max) {
            x *= max/norm;
            y *= max/norm;
        }
        else if (norm < min) {
            x *= min/norm;
            y *= min/norm;
        }
    }

    public static Vec2 lerp(Vec2 a, Vec2 b, float t) {
        return a.add(b.sub(a).scale(t));
    }

    /**
     * Create a random vector with components x and y between [0,xBound) and [0, yBound) respectively
     * @param xBound (Exclusive) maximum x value
     * @param yBound (Exclusive) maximum y value
     * @return New randomly initialized Vec2.
     */
    public static Vec2 random(float xBound, float yBound) {
        return new Vec2(
                ThreadLocalRandom.current().nextFloat(xBound),
                ThreadLocalRandom.current().nextFloat(yBound)
        );
    }

    /**
     * Create a random vector with components x and y between [xOrigin,xBound) and [yOrigin, yBound) respectively
     * @param xOrigin (Inclusive) minimum x value.
     * @param xBound (Exclusive) maximum x value
     * @param yOrigin (Inclusive) minimum y value.
     * @param yBound (Exclusive) maximum y value
     * @return New randomly initialized Vec2.
     */
    public static Vec2 random(float xOrigin, float xBound, float yOrigin, float yBound) {
        return new Vec2(
                ThreadLocalRandom.current().nextFloat(xOrigin, xBound),
                ThreadLocalRandom.current().nextFloat(yOrigin, yBound)
        );
    }

    public static Vec2 randomUnit() {
        double theta = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
        return new Vec2((float) Math.cos(theta), (float) Math.sin(theta));
    }

    public static Vec2 unitFromAngle(float a) {
        return new Vec2((float) Math.cos(a),(float) Math.sin(a));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public Vec2 clone() {
        try {
            return (Vec2) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError("Objecte no clonable"); // No pot passar
        }
    }
}
