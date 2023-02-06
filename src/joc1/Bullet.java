package joc1;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

enum BulletEffect {
    HOMING, BOUNCE, AREA, REPLICATE
}

abstract class Bullet extends PhysicsObject implements Collider {
    boolean friendly;
    double instatiationTime;
    float lifetime;

    private final static String labelF = "bulletF";
    private final static String labelE = "bulletE";
    private final static Set<String> collisionMask = new HashSet<>(List.of(labelF, labelE));
    Shape bulletShape;
    Shape hitbox;

    float damage;
    int penetration;
    Set<BulletEffect> effects;

    Bullet(Joc j, Vec2 pos, float rotation, Vec2 scale, Vec2 speed, boolean friendly, float lifetime, Shape bulletShape,
           float damage, int penetration, Set<BulletEffect> effects) {
        super(j, pos, rotation, scale, speed);
        this.friendly = friendly;
        this.lifetime = lifetime;

        this.damage = damage;
        this.penetration = penetration;
        this.effects = effects;

        if (bulletShape != null){
            this.bulletShape = bulletShape;
            updateCollider();
        }
    }

    void start() {
        instatiationTime = Time.time();
    }

    void fixedUpdate() {
        if (lifetime != 0 && Time.time() - instatiationTime >= lifetime) {
            j.destroy(this);
            return;
        }

        translate(getSpeed().scale((float) Time.deltaTime()));

        updateCollider();
    }

    @Override
    public void onColliderEnter(Collider other) {
        if (friendly && other.getLabel().equals("player")) {
            return;
        }

        if (friendly && other.getLabel().equals("enemy")) {
            if (--penetration <= 0) {
                j.destroy(this);
            }
        }
    }

    @Override
    public void onColliderStay(Collider other) {

    }

    @Override
    public void onColliderExit(Collider other) {

    }

    @Override
    public Shape getCollider() {
        return hitbox;
    }

    @Override
    public void updateCollider() {
        hitbox = getModelMatrix().createTransformedShape(bulletShape);
    }

    @Override
    public String getLabel() {
        if (friendly) {
            return labelF;
        }
        return labelE;
    }

    @Override
    public Set<String> getCollisionMask() {
        return collisionMask;
    }

    @Override
    public boolean colliderIsActive() {
        return !destroying;
    }
}
