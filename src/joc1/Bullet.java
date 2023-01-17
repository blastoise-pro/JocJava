package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

abstract class Bullet {
    private Vec2 pos;
    private Vec2 speed;

    boolean friendly;

    Polygon bulletShape;
    private Vec2 scale;
    Shape hitbox;

    Vec2 getPosition() {
        return pos.clone();
    }

    void setPosition(Vec2 pos) {
        this.pos = pos.clone();
    }

    void translate(Vec2 vec) {
        pos.Add(vec);
    }

    Vec2 getSpeed() {
        return speed.clone();
    }

    void setSpeed(Vec2 speed) {
        this.speed = speed.clone();
    }

    Vec2 getScale() {
        return scale.clone();
    }

    void setScale(Vec2 scale) {
        this.scale = scale.clone();
    }

    Bullet(Vec2 pos, Vec2 speed, boolean friendly, Polygon bulletShape, Vec2 scale) {
        this.pos = pos;
        this.speed = speed;
        this.friendly = friendly;

        this.bulletShape = bulletShape;
        this.scale = scale;
        updateHitbox();
    }

    void move(double deltaTime) {
        pos.Add(speed.scale((float) deltaTime));
        updateHitbox();
    }

    public abstract void pintar(Graphics2D g, AffineTransform PVMatrix);

    private void updateHitbox() {
        hitbox = getModelMatrix().createTransformedShape(bulletShape);
    }

    AffineTransform getModelMatrix() {
        AffineTransform model = AffineTransform.getTranslateInstance(pos.x, pos.y);
        model.rotate(speed.getAngle());
        model.scale(scale.x, scale.y);
        return model;
    }
}
