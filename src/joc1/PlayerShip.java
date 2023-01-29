package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class PlayerShip extends Ship{
    private final static int[] xPoints = {-2, -2,  2};
    private final static int[] yPoints = {-1, 1, -1};

    PlayerShip(Joc j, Vec2 position) {
        super(j, position, 0, new Vec2(1, 1), new Vec2(), 40, 100, 0.9f, Direction.RIGHT, new Vec2(),
                Direction.RIGHT.vector(), 2, 100, 10f,
                new Polygon(xPoints, yPoints, xPoints.length));
        hitbox = shipShape;
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
