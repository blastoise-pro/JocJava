package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class Background extends GameObject {

    private class Layer {
        Sprite sprite;
        float movementFactor;
        Vec2 position;

        Layer(String path, Vec2 scale, float movementFactor) {
            sprite = new Sprite(path, scale);
            this.movementFactor = movementFactor;
        }

        void movementUpdate() {
            position.Add(j.camera.getPosition().sub(lastCameraPos).scale(movementFactor));
            Vec2 diff = lastCameraPos.sub(position);
            System.out.println("Diff: " + diff);
            if (diff.x > sprite.getWidth()/2) {
                position.x += sprite.getWidth();
            }
            if (diff.x < -sprite.getWidth()/2) {
                position.x -= sprite.getWidth();
            }
            if (diff.y > sprite.getHeight()/2) {
                position.y += sprite.getHeight();
            }
            if (diff.y < -sprite.getHeight()/2) {
                position.y -= sprite.getHeight();
            }
        }

        void pintar(Graphics2D g, AffineTransform PVMatrix) {
            AffineTransform PVM = new AffineTransform(PVMatrix);
            PVM.translate(position.x, position.y);
            sprite.pintar(g, PVM);
            AffineTransform sides;
            for (Direction dir:Direction.values()) {
                sides = new AffineTransform(PVM);
                sides.translate(dir.vector().x * sprite.getWidth(), dir.vector().y * sprite.getHeight());
                sprite.pintar(g, sides);
            }
        }
    }

    Layer[] layers = new Layer[3];
    Vec2 lastCameraPos;

    Background(Joc j) {
        super(j, new Vec2(0, 0), 0, new Vec2(1f, 1f));
        layers[0] = new Layer("assets/BG/layer1.png", new Vec2(.2f, .2f), 0.9f);
        layers[1] = new Layer("assets/BG/layer3.png", new Vec2(.2f, .2f), 0.5f);
        layers[2] = new Layer("assets/BG/layer2.png", new Vec2(.2f, .2f), 0.3f);
    }

    void start() {
        lastCameraPos = j.camera.getPosition();
        for (Layer layer:layers) {
            layer.position = j.camera.getPosition();
        }
    }

    void lateUpdate() {
        for (Layer layer:layers) {
            layer.movementUpdate();
        }
        System.out.println("\n");
        lastCameraPos = j.camera.getPosition();
        if (Input.getActionDown(Action.SHOOT)) {
            for (Layer layer:layers) {
                System.out.println("Position: " + layer.position);
            }
            System.out.println("\n\n");
        }
    }

    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        for (Layer layer:layers) {
            layer.pintar(g, PVMatrix);
        }
    }
}
