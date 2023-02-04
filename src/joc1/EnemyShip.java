package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class EnemyShip extends Ship {
    private final static int[] xPoints = {-32, -32, -6, 48, 48, -6};
    private final static int[] yPoints = {30, -30, -30, -14, 14, 30};

    String label = "enemy";
    private final static Set<String> collisionMask = new HashSet<>(List.of("bulletE"));

    Sprite sprite;
    HPBar hpBar;
    Animation explosion;
    float contactDamage = 3;

    EnemyShip(Joc j, Vec2 position) {
        super(j, position, 0, new Vec2(1, 1), new Vec2(), 30,
                80, 1000, 0.95f, 1.5f, Direction.LEFT,
                new Vec2(8, 4), Direction.RIGHT.vector(), 10, 100, 0.5f, null);
        Vec2 hitboxScale = new Vec2(.1f,.1f);
        sprite = new Sprite(AssetLoader.enemyShip1, (float) -Math.PI/2, hitboxScale);
        shipShape = AffineTransform.getScaleInstance(hitboxScale.x, hitboxScale.y)
                .createTransformedShape(new Polygon(xPoints, yPoints, xPoints.length));

        hpBar = new HPBar(j, this, new Vec2(0, -sprite.getHeight()/2 - 2), new Vec2(sprite.getHeight(), 1));
        explosion = new Animation(AssetLoader.explosion1, new Vec2(0.5f, 0.5f), 1, false);
    }

    void update() {
        if (isDead && explosion.getIndex() == 8) {
            drawShip = false;
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
