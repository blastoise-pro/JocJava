package joc1;

abstract class PhysicsObject extends GameObject {
    private Vec2 speed;

    Vec2 getSpeed() {
        return speed.clone();
    }

    void setSpeed(Vec2 speed) {
        this.speed = speed.clone();
    }

    void applyForce(Vec2 direction, float power) {
        speed.Add(direction.scale(power));
    }

    void applyForce(Vec2 force) {
        speed.Add(force);
    }

    PhysicsObject(Joc j, Vec2 position, float rotation, Vec2 scale, Vec2 speed) {
        super(j, position, rotation, scale);
        setSpeed(speed);
    }
}
