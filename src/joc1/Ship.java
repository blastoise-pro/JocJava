package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

abstract class Ship extends PhysicsObject implements Collider {
    float maxHP;
    float HP;
    boolean isDead = false;

    float maxSpeed;
    float thrustPower;
    float turningHelp = 0.03f;
    float airResistance;
    float knockback;

    private Vec2 cannonOffset;
    private Vec2 cannonPos;
    private Vec2 cannonDir;
    float cannonLength;

    float bulletSpeed;
    float attackSpeed;
    double lastShotTime = -1000;

    Shape shipShape;
    Shape hitbox;

    boolean drawShip = true;

    Vec2 getCannonOffset() {
        return cannonOffset.clone();
    }

    void setCannonOffset(Vec2 cannonOffset) {
        this.cannonOffset = cannonOffset.clone();
    }

    Vec2 getCannonPos() {
        return cannonPos.clone();
    }

    void setCannonPos(Vec2 cannonPos) {
        this.cannonPos = cannonPos.clone();
    }

    Vec2 getCannonDir() {
        return cannonDir.clone();
    }

    void setCannonDir(Vec2 cannonDir) {
        this.cannonDir = cannonDir.clone();
    }

    Ship(Joc j, Vec2 position, float rotation, Vec2 scale, Vec2 speed, float maxHP,
         float maxSpeed, float thrustPower, float airResistance, float knockback, Direction lookingAt, Vec2 cannonOffset,
         Vec2 cannonDir, float cannonLength, float bulletSpeed, float attackSpeed, Shape shipShape) {
        super(j, position, rotation, scale, speed);
        this.maxHP = maxHP;
        HP = maxHP;

        this.maxSpeed = maxSpeed;
        this.thrustPower = thrustPower;
        this.airResistance = airResistance;
        this.knockback = knockback;

        setCannonOffset(cannonOffset);
        setCannonPos(getPosition().add(cannonOffset));
        setCannonDir(cannonDir);
        this.cannonLength = cannonLength;

        this.bulletSpeed = bulletSpeed;
        this.attackSpeed = attackSpeed;

        if (shipShape != null) {
            this.shipShape = shipShape;
            updateCollider();
        }
    }

    void fixedUpdate() {
        setSpeed(getSpeed().clamp(0, maxSpeed));
        translate(getSpeed().scale((float) Time.deltaTime()));
        applyForce(getSpeed().normalized().scale(- maxSpeed * airResistance).scale((float) Time.deltaTime()));
        cannonPos = getPosition().add(cannonOffset);

        updateCollider();
    }

    void thrust(Vec2 dir) {
        float parallelSpeed = dir.dot(getSpeed());
        float boostedThrust = thrustPower;
        if (parallelSpeed < 0) {
            boostedThrust -= thrustPower*parallelSpeed*turningHelp;
        }
        applyForce(dir.scale(boostedThrust).scale((float) Time.deltaTime()));
    }

    void collideWithShip(Ship other) {
        Vec2 force = getPosition().sub(other.getPosition()).normalized();
        float relSpeed = getSpeed().sub(other.getSpeed()).norm();
        float power = relSpeed * knockback;
        applyForce(force, power);
        System.out.println("Force applied: " + force.scale(power));
    }

    void pointCannonAt(Vec2 pos) {
        Vec2 dir = pos.sub(cannonPos).normalized();
        if (dir.norm2() == 0)
            return;
        cannonDir = dir;
    }

    AffineTransform getCannonModelMatrix() {
        return AffineTransform.getTranslateInstance(cannonOffset.x, cannonOffset.y);
    }

    @Override
    public Shape getCollider() {
        return hitbox;
    }

    @Override
    public void updateCollider() {
        hitbox = getModelMatrix().createTransformedShape(shipShape);
    }

    @Override
    public boolean colliderIsActive() {
        return !destroying && drawShip;
    }
}
