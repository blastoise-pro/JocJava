package joc1;

import java.awt.*;
import java.util.List;

abstract class Ship extends PhysicsObject implements Collider {
    float maxHP;
    float HP;
    boolean isDead = false;

    float maxSpeed;
    float thrustPower;
    float turningHelp;
    float airResistance;
    float knockback;

    float bulletSpeed;
    float attackSpeed;
    double lastShotTime = -1000;

    Shape shipShape;
    Shape hitbox;

    boolean drawShip = true;

    Ship(Joc j, Vec2 position, float rotation, Vec2 scale, Vec2 speed, float maxHP,
         float maxSpeed, float thrustPower, float turningHelp, float airResistance, float knockback,
         float bulletSpeed, float attackSpeed, Shape shipShape) {
        super(j, position, rotation, scale, speed);
        this.maxHP = maxHP;
        HP = maxHP;

        this.maxSpeed = maxSpeed;
        this.thrustPower = thrustPower;
        this.turningHelp = turningHelp;
        this.airResistance = airResistance;
        this.knockback = knockback;

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
        Vec2 forceDir = getPosition().sub(other.getPosition()).normalized();
        float relSpeed = getSpeed().sub(other.getSpeed()).norm();
        float power = relSpeed * knockback;
        applyForce(forceDir, power);
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
        return !destroying && drawShip && !isDead;
    }
}
