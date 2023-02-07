package joc1;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GUIText extends GUIElement {
    String text;
    Color color;
    Font font;
    float textWidth, textHeight;

    GUIText(Joc j, Vec2 position, float rotation, Vec2 anchor, String text, Color color, Font font, GUIElement parent) {
        super(j, position, rotation, new Vec2(), anchor, parent);
        this.color = color;
        this.font = font;
        updateText(text);
    }

    void updateText(String text) {
        this.text = text;
        FontRenderContext frc = new FontRenderContext(null, true, true);
        float totalHeight = 0;
        float totalWidth = 0;
        for (String s:text.split("\n")) {
            Rectangle2D bounds = font.getStringBounds(s, frc);
            totalHeight += bounds.getHeight();
            totalWidth = (float) Math.max(totalWidth, bounds.getWidth());
        }
        textWidth = totalWidth;
        textHeight = totalHeight;
        float[] lengths = {textWidth, textHeight};
        Camera.inversePMatrixGUI.transform(lengths, 0, lengths, 0, 1);
        GUIElement nextParent = this;
        while (nextParent.parent != null) {
            nextParent = nextParent.parent;
            lengths[0] /= nextParent.getScale().x;
            lengths[1] /= nextParent.getScale().y;
        }
        setScale(new Vec2(lengths[0], lengths[1]));
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PMatrix) {
        AffineTransform projectionModel = new AffineTransform(PMatrix);
        projectionModel.concatenate(getModelMatrix());

        AffineTransform textTrans = new AffineTransform(projectionModel);
        textTrans.scale(1f/textWidth, 1f/textHeight);


        AffineTransform savedT = g.getTransform();
        g.transform(textTrans);
        g.setColor(color);
        g.setFont(font);
        float y = 0;
        for (String s:text.split("\n")) {
            y += g.getFontMetrics().getHeight();
            g.drawString(s, 0, y);
        }

        g.setTransform(savedT);

        for (GUIElement child:childs) {
            child.pintar(g, projectionModel);
        }
    }
}
