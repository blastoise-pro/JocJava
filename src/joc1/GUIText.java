package joc1;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class GUIText extends GUIElement {
    Finestra f;
    String text;
    Color color;
    Font font;

    GUIText(Joc j, Finestra f, Vec2 position, float rotation, Vec2 anchor, String text, Color color, Font font, GUIElement parent) {
        super(j, position, rotation, new Vec2(), anchor, parent);
        this.f = f;
        this.text = text;
        this.color = color;
        this.font = font;
        FontRenderContext frc = new FontRenderContext(null, true, true);
        setScale(new Vec2((float) font.getStringBounds(text, frc).getWidth(), (float) font.getStringBounds(text, frc).getHeight()));
        System.out.println("Scale: " + getScale());
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PMatrix) {
        //AffineTransform projectionModel = new AffineTransform(PMatrix);
        AffineTransform projectionModel = new AffineTransform();
        projectionModel.scale(f.winWidth, f.winHeight);

        projectionModel.translate(getPosition().x, getPosition().y);
        projectionModel.scale(1f/f.winWidth, 1f/f.winHeight);
        projectionModel.rotate(getRotation());
        projectionModel.scale(getScale().x, getScale().y);

        projectionModel.translate(-getAnchor().x, getAnchor().y);
        projectionModel.scale(1f/getScale().x, 1f/getScale().y);

        AffineTransform savedT = g.getTransform();
        //g.transform(projectionModel);
        //g.translate(getPosition().x * 1500, getPosition().y * 900);
        //g.rotate(getRotation());
        //g.translate(-getAnchor().x * getScale().x, getAnchor().y * getScale().y);
        g.transform(projectionModel);
        //g.scale(3, 3);
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, 0, 0);
        g.setTransform(savedT);

        for (GUIElement child:childs) {
            child.pintar(g, projectionModel);
        }
    }
}
