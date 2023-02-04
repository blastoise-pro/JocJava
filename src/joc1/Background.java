package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

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
            //System.out.println("Diff: " + diff);
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
            for (Direction8 dir: Direction8.values()) {
                sides = new AffineTransform(PVM);
                sides.translate(dir.vector().x * sprite.getWidth(), dir.vector().y * sprite.getHeight());
                sprite.pintar(g, sides);
            }
        }
    }

    List<Layer> layers = new ArrayList<>();
    Vec2 lastCameraPos;

    Background(Joc j) {
        super(j, new Vec2(0, 0), 0, new Vec2(1f, 1f));
        layers.add(new Layer("assets/BG/layer1.png", new Vec2(.2f, .2f), 0.9f));
        //layers.add(new Layer("assets/BG/space_background_pack/layers/parallax-space-backgound.png", new Vec2(1f, 1f), 0.9f));
        //layers.add(new Layer("assets/BG/layer3.png", new Vec2(.2f, .2f), 0.5f));
        layers.add(new Layer("assets/BG/layer2.png", new Vec2(.2f, .2f), 0.7f));
        layers.add(new Layer("assets/BG/space_background_pack/layers/parallax-space-stars.png", new Vec2(.5f, .5f), 0.5f));
        layers.add(new Layer("assets/BG/space_background_pack/layers/parallax-space-far-planets.png", new Vec2(1f, 1f), 0.3f));
        //layers.add(new Layer("assets/BG/space_background_pack/layers/parallax-space-ring-planet.png", new Vec2(1f, 1f), 0.3f));
        //layers.add(new Layer("assets/BG/space_background_pack/layers/parallax-space-big-planet.png", new Vec2(1f, 1f), 0.2f));
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
        lastCameraPos = j.camera.getPosition();
    }

    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        for (Layer layer:layers) {
            layer.pintar(g, PVMatrix);
        }
    }
}
