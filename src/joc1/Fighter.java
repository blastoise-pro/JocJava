package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class Fighter extends EnemyShip {
    private final static int[] xPoints = {0, -14, 0, 10};
    private final static int[] yPoints = {-13, 0, 13, 0};
    static final float baseHP = 3;

    Fighter(Joc j, Vec2 position) {
        super(j, position, baseHP, 80, 800, 0f, 1.5f, 5);
        Vec2 hitboxScale = new Vec2(.2f,.2f);
        sprite = new Sprite(AssetLoader.enemyFighter, (float) -Math.PI/2, hitboxScale);
        shipShape = AffineTransform.getScaleInstance(hitboxScale.x, hitboxScale.y)
                .createTransformedShape(new Polygon(xPoints, yPoints, xPoints.length));
        updateCollider();
        hpBar = new HPBar(j, this, new Vec2(0, -sprite.getHeight()/2), new Vec2(sprite.getHeight(), 1));
        explosion = new Animation(AssetLoader.explosionSmall, new Vec2(0.3f, 0.3f), 0.7f, false);
    }

    void start() {
        setSpeed(j.playerShip.getPosition().sub(getPosition()).normalized().scale(maxSpeed));
    }

    void fixedUpdate() {
        Vec2 toPlayer = j.playerShip.getPosition().sub(getPosition());
        float distance = toPlayer.norm()/1000;
        thrust(toPlayer.scale(distance * distance * distance));
        setRotation(getSpeed().getAngle());

        super.fixedUpdate();
    }
}
