package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class BasicBullet extends Bullet{
    private final static int[] xPoints = {-8, -1, 1, 8,  1, -1};
    private final static int[] yPoints = { 0,  2, 2, 0, -2, -2};
    private final static Vec2 defaultScale = new Vec2(0.2f, 0.2f);

    BasicBullet(Vec2 pos, Vec2 speed, boolean friendly) {
        super(pos.clone(), speed.clone(), friendly, new Polygon(xPoints, yPoints, xPoints.length), defaultScale);
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
