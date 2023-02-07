package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

class XPBar extends GUIElement {
    private final static Color colorBottom = Color.blue;
    private final static Color colorTop = Color.cyan;
    private final Rectangle2D BGRect;
    private final Rectangle2D frontRect;
    XPBar(Joc j, Vec2 positon, float rotation, Vec2 dimensions, Vec2 anchor, GUIElement parent) {
        super(j, positon, rotation, dimensions, anchor, parent);
        BGRect = new Rectangle2D.Float(0f, 0f, 1f, 1f);
        frontRect = new Rectangle2D.Float(0f, 0f, 1f, 1f);
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PMatrix) {
        AffineTransform barPos = new AffineTransform(PMatrix);
        barPos.concatenate(getModelMatrix());
        Shape bgshape = barPos.createTransformedShape(BGRect);
        g.setColor(Color.gray);
        g.fill(bgshape);

        float percent = (float) (j.playerShip.totalXP - j.playerShip.lastXPLevel) / (j.playerShip.nextXPlevel - j.playerShip.lastXPLevel);
        AffineTransform frontTrans = new AffineTransform(barPos);
        frontTrans.scale(percent, 1);
        float[] barpoints = {0, 0, 1, 0};
        frontTrans.transform(barpoints, 0, barpoints, 0, 2);
        GradientPaint gradient = new GradientPaint(barpoints[0], barpoints[1], colorBottom, barpoints[2], barpoints[3], colorTop);
        Shape frontShape = frontTrans.createTransformedShape(frontRect);
        g.setPaint(gradient);
        g.fill(frontShape);

        for (GUIElement child:childs) {
            child.pintar(g, barPos);
        }
    }
}
