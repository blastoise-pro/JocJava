package joc1;

import java.awt.*;
import java.util.EnumSet;
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
    Animation explosion;

    float damage;
    int penetration;
    Set<BulletEffect> effects;
    Ship target;
    float homingRange = 30;
    float rotationSpeed = 2 * (float) Math.PI;
    int bounceCount;
    float bounceRange = 25;
    float explosionRange = 15;

    Bullet(Joc j, Vec2 pos, float rotation, Vec2 scale, Vec2 speed, boolean friendly, float lifetime, Shape bulletShape,
           float damage, int penetration, int bounceCount, Set<BulletEffect> effects) {
        super(j, pos, rotation, scale, speed);
        this.friendly = friendly;
        this.lifetime = lifetime;

        this.damage = damage;
        this.penetration = penetration;
        this.bounceCount = bounceCount;
        this.effects = EnumSet.copyOf(effects);

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

        if (effects.contains(BulletEffect.HOMING))
        {
            if (target == null && friendly) {
                for (EnemyShip enemy:j.gameController.enemyList) {
                    if (!enemy.isDead && enemy.getPosition().sub(getPosition()).norm2() < homingRange*homingRange) {
                        target = enemy;
                        break;
                    }
                }
            }
            if (target != null && !target.isDead) {
                float rotateAngle = target.getPosition().sub(getPosition()).getAngle(getSpeed());
                if (rotateAngle > 0) {
                    rotateAngle = -1;
                }
                else if (rotateAngle < 0) {
                    rotateAngle = 1;
                }
                else {
                    rotateAngle = 0;
                }
                setSpeed(getSpeed().rotate(rotateAngle * rotationSpeed * (float) Time.deltaTime()));
            }
        }

        translate(getSpeed().scale((float) Time.deltaTime()));
        setRotation(getSpeed().getAngle());

        updateCollider();
    }

    @Override
    public void onColliderEnter(Collider other) {
        if (friendly && other.getLabel().equals("player")) {
            return;
        }
        if (friendly && other.getLabel().equals("enemy")) {
            if (effects.contains(BulletEffect.HOMING)) {
                target = null;
                effects.remove(BulletEffect.HOMING);
            }

            if (effects.contains(BulletEffect.AREA)) {
                explosion.play();
                for (EnemyShip enemy:j.gameController.enemyList) {
                    if (enemy.getPosition().sub(getPosition()).norm2() < explosionRange*explosionRange) {
                        enemy.onColliderEnter(this);
                    }
                }
            }

            if (bounceCount > 0 && effects.contains(BulletEffect.BOUNCE)) {
                for (EnemyShip enemy:j.gameController.enemyList) {
                    if (enemy == other || enemy.isDead) continue;
                    Vec2 toEnemy = enemy.getPosition().sub(getPosition());
                    float toEnemyDist = toEnemy.norm();
                    if (toEnemyDist < bounceRange) {
                        System.out.println("Bouncing to enemy");
                        setSpeed(getSpeed().rotate(getSpeed().getAngle(toEnemy)));
                        bounceCount--;
                        return;
                    }
                }
            }

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
