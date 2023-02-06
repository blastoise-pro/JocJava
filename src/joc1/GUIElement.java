package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

abstract class GUIElement extends GameObject {
    private Vec2 anchor;
    List<GUIElement> childs = new ArrayList<>();

    Vec2 getAnchor() {
        return anchor.clone();
    }

    void setAnchor(Vec2 anchor) {
        this.anchor = anchor.clone();
    }
    GUIElement(Joc j, Vec2 position, float rotation, Vec2 dimensions, Vec2 anchor, GUIElement parent) {
        super(j, position, 0, dimensions);
        setAnchor(anchor);
        if (parent != null) {
            isChild = true;
            parent.childs.add(this);
        }
    }

    @Override
    AffineTransform getModelMatrix() {
        AffineTransform model = super.getModelMatrix();
        model.translate(-anchor.x, -anchor.y);
        return model;
    }
}
