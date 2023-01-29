package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

abstract class Bullet extends PhysicsObject {
    boolean friendly;
    double instatiationTime;
    float lifetime;

    Polygon bulletShape;
    Shape hitbox;

    Bullet(Joc j, Vec2 pos, float rotation, Vec2 scale, Vec2 speed, boolean friendly, float lifetime, Polygon bulletShape) {
        super(j, pos, rotation, scale, speed);
        this.friendly = friendly;
        this.lifetime = lifetime;

        this.bulletShape = bulletShape;
        updateHitbox();
    }

    void start() {
        instatiationTime = Time.time();
    }

    @Override
    void fixedUpdate() {
        if (lifetime != 0 && Time.time() - instatiationTime >= lifetime) {
            j.destroy(this);
            return;
        }

        translate(getSpeed().scale((float) Time.deltaTime()));

        updateHitbox();
    }

    private void updateHitbox() {
        hitbox = getModelMatrix().createTransformedShape(bulletShape);
    }
}
