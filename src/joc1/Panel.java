package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class Panel extends GUIElement implements MouseReciever {
    //Sprite sprite;
    BufferedImage image;
    float imageScalex, imageScaley;
    Panel(Joc j, Vec2 position, float rotation, Vec2 dimensions, Vec2 anchor, BufferedImage image, GUIElement parent) {
        super(j, position, rotation, dimensions, anchor, parent);
        /*
        if (image != null) {
            sprite = new Sprite(image);
            sprite.setAnchor(new Vec2(0, 0));
            sprite.setScale(new Vec2(1/sprite.getWidth(), -1/sprite.getHeight()));
        }
        */
        if (image != null){
            this.image = image;
            imageScalex = 1f / image.getWidth();
            imageScaley = 1f / image.getHeight();
        }
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PMatrix) {
        AffineTransform projectionModel = new AffineTransform(PMatrix);
        projectionModel.concatenate(getModelMatrix());

        if (image != null) {
            AffineTransform imageTrans = new AffineTransform(projectionModel);
            imageTrans.scale(imageScalex, imageScaley);
            g.drawImage(image, imageTrans, null);
        }

        for (GUIElement child:childs) {
            child.pintar(g, projectionModel);
        }
    }

    @Override
    public boolean containsCoords(Vec2 coords) {
        Vec2 topLeft = getPosition().sub(getAnchor().cscale(getScale()));
        Vec2 bottomRight = topLeft.add(getScale());
        return topLeft.x < coords.x && topLeft.y < coords.y && bottomRight.x > coords.x && bottomRight.y > coords.y;
    }

    @Override
    public void onMouseEnter() {

    }

    @Override
    public void onMouseExit() {

    }

    @Override
    public void onMouseClick() {

    }
}
