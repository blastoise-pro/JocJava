package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

class Camera extends GameObject {
    private final Finestra f;
    // Parametres del "frustum" de la projecció ortogràfica (quines coordenades a view space són visibles)
    // Només necessitem els límits esquerre i dret, el superior i inferior es calculen respectant l'aspect ratio
    // de la finestra
    float l = -100, r = 100, t, b;
    AffineTransform viewMatrix;
    AffineTransform projectionMatrix;
    AffineTransform inversePVMatrix;

    Camera(Joc j, Vec2 position, Finestra f) {
        super(j, position, 0, new Vec2());
        this.f = f;

        updateViewMatrix();
        updateProjectionMatrix();
        updateInversePVMatrix();
    }

    void lateUpdate() {
        Vec2 screenVector = Input.getWindowMousePosition();
        screenVector.x -= 0.5;
        screenVector.y -= 0.5;
        setPosition(j.playerShip.getPosition().add(screenVector.scale(100)));
    }

    void updateViewMatrix() {
        Vec2 camPos = getPosition();
        viewMatrix = AffineTransform.getTranslateInstance(-camPos.x, -camPos.y);
        viewMatrix.rotate(-getRotation());
    }

    void updateProjectionMatrix() {
        float aspectRatio = (float) f.winHeight/f.winWidth;
        t =  (r-l)*aspectRatio/2;
        b = -(r-l)*aspectRatio/2;
        projectionMatrix = new AffineTransform();
        projectionMatrix.translate(-f.winWidth*l/(r-l), f.winHeight*t/(t-b));
        projectionMatrix.scale(f.winWidth/(r-l), -f.winHeight/(t-b));
        System.out.println("t: " + t + " b: " + b);
        System.out.println("winHeight: " + f.winHeight + " winWidth: " + f.winWidth);
    }

    void updateInversePVMatrix() {
        AffineTransform PVMatrix = new AffineTransform(projectionMatrix);
        PVMatrix.concatenate(viewMatrix);
        try {
            inversePVMatrix = PVMatrix.createInverse();
        }
        catch (NoninvertibleTransformException e) {
            throw new AssertionError("Transformació no té inversa"); // No pot passar
        }
    }

    public void pintar(Graphics2D g, AffineTransform PVMatrix) {

    }
}
