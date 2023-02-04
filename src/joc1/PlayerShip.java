package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

class PlayerShip extends Ship {
    private final static int[] xPoints = {-26, -26, -14, -8, 30, -8, -14};
    private final static int[] yPoints = {36, -36, -36, -24, 0, 24, 36};

    private final static String label = "player";
    private final static Set<String> collisionMask = new HashSet<>(List.of("bulletF"));

    Sprite sprite;
    Animation explosion;
    HPBar hpBar;
    double lastDamageTime = 0;
    double invTime = 0.5f;

    PlayerShip(Joc j, Vec2 position) {
        super(j, position, 0, new Vec2(1f, 1f), new Vec2(), 10,
                40, 100, 0.03f, 0.9f, 2,
                new Vec2(), Direction4.RIGHT.vector(), 2, 100, 1f, null);

        Vec2 hitboxScale = new Vec2(0.1f, 0.1f);
        sprite = new Sprite("assets/Ships/basic1.png", hitboxScale);
        shipShape = AffineTransform.getScaleInstance(hitboxScale.x, hitboxScale.y)
                .createTransformedShape(new Polygon(xPoints, yPoints, xPoints.length));

        hpBar = new HPBar(j, this, new Vec2(0, -sprite.getHeight()/2 - 2), new Vec2(sprite.getHeight(), 1));
        explosion = new Animation(AssetLoader.explosionHuge, new Vec2(.5f, .5f), 1.5, false);
    }

    void update() {
        if (j.gamePaused) {
            return;
        }
        if (isDead) {
            if (explosion.getIndex() == 33) {
                drawShip = false;
            }
            return;
        }

        pointCannonAt(Input.getMousePosition());
        if (Input.getAction(Action.SHOOT) && (Time.time() - lastShotTime >= 1/attackSpeed)) {
            shoot();
            System.out.println("Clicked at: " + Input.getMousePosition());
        }
    }

    void fixedUpdate() {
        if (isDead) {
            return;
        }
        setRotation(Input.getMousePosition().sub(getPosition()).getAngle());
        thrust(Input.getDirection());

        super.fixedUpdate();
    }

    void shoot() {
        lastShotTime = Time.time();
        new BasicBullet(j, getCannonPos(), getCannonDir().scale(bulletSpeed), true);
    }

    @Override
    public void onColliderEnter(Collider other) {
        System.out.println("Collider " + getLabel() + " collided with " + other.getLabel());
        if (other.getLabel().equals("enemy")) {
            EnemyShip enemy = (EnemyShip) other;
            collideWithShip(enemy);
            if (Time.time() > lastDamageTime + invTime) {
                lastDamageTime = Time.time();
                damage(enemy.contactDamage);
            }
        }
        else if (other.getLabel().equals("bulletE")) {
            System.out.println("Damage");
            //j.destroy(this);
            damage(3);
        }
    }

    void damage(float amount) {
        HP -= amount;
        if (HP <= 0 && !isDead) {
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
        g.setColor(Color.cyan);
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

        /*
        Vec2 cannonDR = getCannonDir().scale(cannonLength);
        Vec2 cannonPos = getCannonPos();
        float[] cannonPoints = {cannonPos.x, cannonPos.y, cannonPos.x + cannonDR.x, cannonPos.y + cannonDR.y};
        PVMatrix.transform(cannonPoints, 0, cannonPoints, 0, cannonPoints.length/2);
        g.drawLine((int) cannonPoints[0], (int) cannonPoints[1], (int) cannonPoints[2], (int) cannonPoints[3]);
         */
    }
}
