package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.EnumSet;
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
    float baseHP;

    EnemyShip(Joc j, Vec2 position, float maxHP, float maxSpeed, float thrustPower, float airResistance, float knockback, float contactDamage) {
        super(j, position, 0, new Vec2(1, 1), new Vec2(), maxHP,
                maxSpeed, thrustPower, 0, airResistance, knockback,
                100, 0.5f, null);
        baseHP = maxHP;
        this.maxHP = (float) (Time.time() - j.gameController.startTime) * baseHP * 0.03f + baseHP;
        this.HP = this.maxHP;
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
            Bullet bullet = (Bullet) other;
            damage(bullet);
        }
        else if (other.getLabel().equals("player")) {
            collideWithShip((Ship) other);
        }
        else if (other.getLabel().equals("enemy")) {
            collideWithShip((Ship) other);
        }
    }

    private void damage(Bullet bullet) {
        HP -= bullet.damage;
        if (HP <= 0 && !isDead) {
            HP = 0;
            isDead = true;
            explosion.play();
            new Gem(j, getPosition(), (int) baseHP);
            if (bullet.effects.contains(BulletEffect.REPLICATE)) {
                int rand = j.ran.nextInt(2);
                Direction[] dirs;
                if (rand == 1) {
                    dirs = Direction.orthogonals();
                }
                else {
                    dirs = Direction.diagonals();
                }
                Set<BulletEffect> spawnEffects = EnumSet.copyOf(bullet.effects);
                spawnEffects.remove(BulletEffect.HOMING);
                for (Direction dir:dirs) {
                    new BasicBullet(j, getPosition(), dir.vector().scale(j.playerShip.bulletSpeed), j.playerShip.bulletSize,
                            true, j.playerShip.attackDamage, j.playerShip.bulletPenetration, j.playerShip.bulletBounces, spawnEffects);
                }
            }
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
