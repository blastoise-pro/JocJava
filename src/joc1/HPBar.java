package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class HPBar extends GameObject {
    static float rangeDown = 0.5f;
    static float rangeUp = 3f;
    static float hueMin = 0;
    static float hueMax = 0.5f;
    private final Ship parent;
    private final Vec2 offset;
    private final Rectangle2D BGRect;
    private final Rectangle2D frontRect;
    Color startColor;
    Color endColor;

    HPBar(Joc j, Ship parent, Vec2 offset, Vec2 scale) {
        super(j, new Vec2(), 0, scale);
        BGRect = new Rectangle2D.Float(-0.5f, -0.5f, 1f, 1f);
        frontRect = new Rectangle2D.Float(0f, -0.5f, 1f, 1f);
        this.parent = parent;
        this.offset = offset.clone();
    }

    void start() {
        if (parent.getLabel().equals("player")) {
            startColor = Color.blue;
        }
        else {
            startColor = Color.red;
        }
        float diff = parent.maxHP/j.playerShip.maxHP;
        if (diff > rangeUp) {
            diff = rangeUp;
        }
        else if (diff < rangeDown) {
            diff = rangeDown;
        }
        float percentOld = (diff - rangeDown)/(rangeUp - rangeDown);
        float hueval = percentOld * (hueMax - hueMin) + hueMin;
        endColor = Color.getHSBColor(hueval, 1f, 1f);
    }

    void lateUpdate() {
        if (parent.isDead) {
            j.destroy(this);
            return;
        }
        setPosition(parent.getPosition().add(offset));
    }

    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        // Background
        AffineTransform barpos = new AffineTransform(PVMatrix);
        barpos.concatenate(getModelMatrix());
        Shape back = barpos.createTransformedShape(BGRect);
        g.setColor(Color.darkGray);
        g.fill(back);

        // Front
        barpos.translate(-0.5f, 0f);
        barpos.scale(Math.max(parent.HP/parent.maxHP, 0), 1);
        float[] barpoints = {0, 0, 1, 0};
        barpos.transform(barpoints, 0, barpoints, 0, 2);
        GradientPaint gradient = new GradientPaint(barpoints[0], barpoints[1], startColor, barpoints[2], barpoints[3], endColor);
        Shape front = barpos.createTransformedShape(frontRect);
        g.setPaint(gradient);
        g.fill(front);
    }
}
