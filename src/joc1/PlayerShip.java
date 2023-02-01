package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

class PlayerShip extends Ship {
    private final static int[] xPoints = {-2, -2,  2};
    private final static int[] yPoints = {-1, 1, -1};

    private final static String label = "player";
    private final static Set<String> collisionMask = new HashSet<>(List.of("bulletF"));

    PlayerShip(Joc j, Vec2 position) {
        super(j, position, 0, new Vec2(1, 1), new Vec2(), 40, 100, 0.9f, Direction.RIGHT, new Vec2(),
                Direction.RIGHT.vector(), 2, 100, 1f,
                new Polygon(xPoints, yPoints, xPoints.length));
    }

    void update() {
        pointCannonAt(Input.getMousePosition());
        if (Input.getAction(Action.SHOOT) && (Time.time() - lastShotTime >= 1/attackSpeed)) {
            shoot();
        }
    }

    void fixedUpdate() {
        thrust(Input.getDirection());

        super.fixedUpdate();
    }

    void shoot() {
        lastShotTime = Time.time();
        new BasicBullet(j, getCannonPos(), getCannonDir().scale(bulletSpeed), true);
    }

    @Override
    public void onColliderEnter(Collider other) {
        if (other.getLabel().equals("enemy") || other.getLabel().equals("bulletE")) {
            System.out.println("Damage");
            j.destroy(this);
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
        g.setColor(Color.cyan);
        AffineTransform PVM = new AffineTransform(PVMatrix);
        PVM.concatenate(getModelMatrix());
        Shape transPoly = PVM.createTransformedShape(shipShape);
        g.fill(transPoly);

        Vec2 cannonDR = getCannonDir().scale(cannonLength);
        Vec2 cannonPos = getCannonPos();
        float[] cannonPoints = {cannonPos.x, cannonPos.y, cannonPos.x + cannonDR.x, cannonPos.y + cannonDR.y};
        PVMatrix.transform(cannonPoints, 0, cannonPoints, 0, cannonPoints.length/2);
        g.drawLine((int) cannonPoints[0], (int) cannonPoints[1], (int) cannonPoints[2], (int) cannonPoints[3]);
    }
}
