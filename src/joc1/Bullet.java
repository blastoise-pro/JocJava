package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

abstract class Bullet extends PhysicsObject {
    boolean friendly;

    Polygon bulletShape;
    Shape hitbox;

    Bullet(Joc j, Vec2 pos, float rotation, Vec2 scale, Vec2 speed, boolean friendly, Polygon bulletShape) {
        super(j, pos, rotation, scale, speed);
        this.friendly = friendly;

        this.bulletShape = bulletShape;
        updateHitbox();
    }

    @Override
    void fixedUpdate() {
        translate(getSpeed().scale((float) Time.deltaTime()));

        updateHitbox();
    }

    private void updateHitbox() {
        hitbox = getModelMatrix().createTransformedShape(bulletShape);
    }
}
