package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract class EnemyShip extends Ship {
    String label = "enemy";
    private final static Set<String> collisionMask = new HashSet<>(List.of("bulletE"));

    Sprite sprite;
    HPBar hpBar;
    Animation explosion;
    float contactDamage;

    EnemyShip(Joc j, Vec2 position, float maxHP, float maxSpeed, float thrustPower, float airResistance, float knockback, float contactDamage) {
        super(j, position, 0, new Vec2(1, 1), new Vec2(), maxHP,
                maxSpeed, thrustPower, 0, airResistance, knockback,
                new Vec2(8, 4), Direction4.RIGHT.vector(), 10, 100, 0.5f, null);
        this.contactDamage = contactDamage;
    }

    void update() {
        if (isDead){
            if(explosion.getIndex() == 8) {
                drawShip = false;
            }
            if (!explosion.isPlaying()) {
                j.destroy(this);
            }
        }
    }

    @Override
    public void onColliderEnter(Collider other) {
        if (other.getLabel().equals("bulletF")) {
            damage(3);
        }
        else if (other.getLabel().equals("player")) {
            System.out.println("Enemy collided with player.");
            collideWithShip((Ship) other);
        }
        else if (other.getLabel().equals("enemy")) {
            collideWithShip((Ship) other);
        }
    }

    void damage(float amount) {
        HP -= amount;
        if (HP <= 0) {
            HP = 0;
            isDead = true;
            explosion.play();
        }
    }

    @Override
    public void onColliderStay(Collider other) {
        if (other.getLabel().equals("enemy") || other.getLabel().equals("player")) {
            knockback *= 2;
            collideWithShip((Ship) other);
            knockback *= 0.5f;
        }
    }

    @Override
    public void onColliderExit(Collider other) {

    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Set<String> getCollisionMask() {
        return collisionMask;
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        g.setColor(Color.red);
        AffineTransform PVM = new AffineTransform(PVMatrix);
        PVM.concatenate(getModelMatrix());

        if (drawShip){
            sprite.pintar(g, PVM);
        }

        if (Joc.DRAW_HITBOXES) {
            Shape transPoly = PVM.createTransformedShape(shipShape);
            g.fill(transPoly);
        }

        if (explosion.isPlaying()) {
            explosion.getFrame().pintar(g, PVM);
        }
    }
}
