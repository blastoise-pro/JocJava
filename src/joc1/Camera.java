package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class Camera extends GameObject {
    private AffineTransform viewMatrix;

    Camera(Joc j, Vec2 position) {
        super(j, position, 0, new Vec2());
        updateViewMatrix();
    }

    void lateUpdate() {
        setPosition(j.playerShip.getPosition());
    }

    void updateViewMatrix() {
        Vec2 camPos = getPosition();
        viewMatrix = AffineTransform.getTranslateInstance(-camPos.x, -camPos.y);
        viewMatrix.rotate(-getRotation());
    }

    AffineTransform getViewMatrix() {
        return viewMatrix;
    }

    public void pintar(Graphics2D g, AffineTransform PVMatrix) {

    }
}
