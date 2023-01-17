package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class PlayerShip extends Ship{
    private final static int[] xPoints = {-2, -2,  2};
    private final static int[] yPoints = {-1, 1, -1};

    PlayerShip(Vec2 pos) {
        super(pos, 0, new Vec2(1, 1), new Vec2(), 60, 100, 0.9f, Direction.RIGHT, new Vec2(),
                Direction.RIGHT.vector(), 2, 100, 1f,
                new Polygon(xPoints, yPoints, xPoints.length));
        hitbox = shipShape;
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

    Bullet shoot() {
        lastShotTime = System.currentTimeMillis()/1000.0;
        return new BasicBullet(getCannonPos(), getCannonDir().scale(bulletSpeed), true);
    }
}
