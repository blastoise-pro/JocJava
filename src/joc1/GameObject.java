package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

abstract class GameObject {
    Joc j;
    private Vec2 position;
    private float rotation;
    private Vec2 scale;
    boolean destroying = false;
    boolean isChild;

    Vec2 getPosition() {
        return position.clone();
    }

    void setPosition(Vec2 position) {
        this.position = position.clone();
    }

    void translate(Vec2 dx) {
        position.Add(dx);
    }

    float getRotation() {
        return rotation;
    }

    void setRotation(float rotation) {
        this.rotation = rotation;
    }

    void rotate(float angle) {
        rotation += angle;
    }

    Vec2 getScale() {
        return scale.clone();
    }

    void setScale(Vec2 scale) {
        this.scale = scale.clone();
    }

    void scale(float scalar) {
        scale.Scale(scalar);
    }

    void scale(Vec2 scales) {
        scale.cScale(scales);
    }

    GameObject(Joc j, Vec2 position, float rotation, Vec2 scale) {
        setPosition(position);
        setRotation(rotation);
        setScale(scale);
        isChild = false;
        this.j = j;
        j.addObject(this);
    }

    GameObject(Joc j, Vec2 position, float rotation, Vec2 scale, boolean isChild) {
        setPosition(position);
        setRotation(rotation);
        setScale(scale);
        this.isChild = isChild;
        this.j = j;
        j.addObject(this);
    }

    void start() {

    }

    void update() {

    }

    void fixedUpdate() {

    }

    void lateUpdate() {

    }

    public abstract void pintar(Graphics2D g, AffineTransform PVMatrix);

    AffineTransform getModelMatrix() {
        AffineTransform model = AffineTransform.getTranslateInstance(position.x, position.y);
        model.rotate(rotation);
        model.scale(scale.x, scale.y);
        return model;
    }
}
