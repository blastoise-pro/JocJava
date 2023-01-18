package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class EnemyShip extends Ship {
    private final static int[] xPoints = {0, 0,  4};
    private final static int[] yPoints = {0, 2, 0};

    EnemyShip(Joc j, Vec2 position) {
        super(j, position, 0, new Vec2(1, 1), new Vec2(), 80, 1000, 0.95f, Direction.LEFT, new Vec2(8, 4),
                Direction.RIGHT.vector(), 10, 100, 0.5f,
                new Polygon(xPoints, yPoints, xPoints.length));
        hitbox = shipShape;
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        AffineTransform PVM = new AffineTransform(PVMatrix);
        PVM.concatenate(getModelMatrix());
        Shape transPoly = PVM.createTransformedShape(shipShape);
        g.setColor(Color.red);
        g.fill(transPoly);
    }

    Bullet shoot() {
        return null;
    }
}
