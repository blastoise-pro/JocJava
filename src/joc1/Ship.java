package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

abstract class Ship extends PhysicsObject {
    float maxSpeed;
    float thrustPower;
    float airResistance;
    Direction lookingAt;

    private Vec2 cannonOffset;
    private Vec2 cannonPos;
    private Vec2 cannonDir;
    float cannonLength;

    float bulletSpeed;
    float attackSpeed;
    double lastShotTime = 0;

    Polygon shipShape;
    Shape hitbox;

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

    Ship(Vec2 position, float rotation, Vec2 scale, Vec2 speed,
         float maxSpeed, float thrustPower, float airResistance, Direction lookingAt, Vec2 cannonOffset,
         Vec2 cannonDir, float cannonLength, float bulletSpeed, float attackSpeed, Polygon shipShape) {
        super(position, rotation, scale, speed);

        this.maxSpeed = maxSpeed;
        this.thrustPower = thrustPower;
        this.airResistance = airResistance;
        this.lookingAt = lookingAt;

        setCannonOffset(cannonOffset);
        setCannonPos(getPosition().add(cannonOffset));
        setCannonDir(cannonDir);
        this.cannonLength = cannonLength;

        this.bulletSpeed = bulletSpeed;
        this.attackSpeed = attackSpeed;

        this.shipShape = shipShape;
        updateHitbox();
    }

    void update() {

    }

    void fixedUpdate(double deltaTime) {
        translate(getSpeed().scale((float) deltaTime));
        applyForce(getSpeed().normalized().scale(- maxSpeed * airResistance).scale((float) deltaTime));
        cannonPos = getPosition().add(cannonOffset);

        updateHitbox();
    }

    void thrust(Vec2 dir, double deltaTime) {
        float parallelSpeed = dir.dot(getSpeed());
        float boostedThrust = thrustPower;
        if (parallelSpeed < 0) {
            boostedThrust -= thrustPower*parallelSpeed*0.03;
        }
        applyForce(dir.scale(boostedThrust).scale((float) deltaTime));
        setSpeed(getSpeed().clamp(0, maxSpeed));
    }

    void pointCannonAt(Vec2 pos) {
        cannonDir = pos.sub(cannonPos).normalized();
    }

    abstract Bullet shoot();

    public abstract void pintar(Graphics2D g, AffineTransform PVMatrix);

    private void updateHitbox() {
        hitbox = getModelMatrix().createTransformedShape(shipShape);
    }

    AffineTransform getCannonModelMatrix() {
        return AffineTransform.getTranslateInstance(cannonOffset.x, cannonOffset.y);
    }
}
