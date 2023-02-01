package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class EnemyShip extends Ship {
    private final static int[] xPoints = {0, 0,  4};
    private final static int[] yPoints = {0, 2, 0};

    String label = "enemy";
    private final static Set<String> collisionMask = new HashSet<>(List.of("bulletE"));

    EnemyShip(Joc j, Vec2 position) {
        super(j, position, 0, new Vec2(1, 1), new Vec2(), 80, 1000, 0.95f, Direction.LEFT, new Vec2(8, 4),
                Direction.RIGHT.vector(), 10, 100, 0.5f,
                new Polygon(xPoints, yPoints, xPoints.length));
    }

    @Override
    public void onColliderEnter(Collider other) {
        if (other.getLabel().equals("bulletF")) {
            //j.destroy(this);
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
        AffineTransform PVM = new AffineTransform(PVMatrix);
        PVM.concatenate(getModelMatrix());
        Shape transPoly = PVM.createTransformedShape(shipShape);
        g.setColor(Color.red);
        g.fill(transPoly);
    }
}
