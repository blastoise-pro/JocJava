package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Set;

public class BasicBullet extends Bullet {
    private final static int[] xPoints = {-8, -1, 1, 8,  1, -1};
    private final static int[] yPoints = { 0,  2, 2, 0, -2, -2};
    private final static Vec2 defaultScale = new Vec2(0.2f, 0.2f);

    BasicBullet(Joc j, Vec2 pos, Vec2 speed, boolean friendly, float damage, int penetration, Set<BulletEffect> effects) {
        super(j, pos, speed.getAngle(), defaultScale, speed, friendly, 3, new Polygon(xPoints, yPoints, xPoints.length),
                damage, penetration, effects);
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        if (friendly) g.setColor(Color.white);
        else g.setColor(Color.magenta);
        AffineTransform PVM = new AffineTransform(PVMatrix);
        PVM.concatenate(getModelMatrix());
        Shape transPoly = PVM.createTransformedShape(bulletShape);
        g.fill(transPoly);
    }
}
