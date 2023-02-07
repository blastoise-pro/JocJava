package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class Frigate extends EnemyShip {
    private final static int[] xPoints = {-15, -15, 22, 22};
    private final static int[] yPoints = {20, -20, -8, 8};
    static final float baseHP = 12;

    Frigate(Joc j, Vec2 position) {
        super(j, position, baseHP, 30, 100, 0.9f, 2f, 2);
        Vec2 hitboxScale = new Vec2(.2f,.2f);
        sprite = new Sprite(AssetLoader.enemyFrigate, (float) -Math.PI/2, hitboxScale);
        shipShape = AffineTransform.getScaleInstance(hitboxScale.x, hitboxScale.y)
                .createTransformedShape(new Polygon(xPoints, yPoints, xPoints.length));
        updateCollider();
        hpBar = new HPBar(j, this, new Vec2(0, -sprite.getHeight()/2), new Vec2(sprite.getHeight(), 1));
        explosion = new Animation(AssetLoader.explosionSmall, new Vec2(0.5f, 0.5f), 0.7f, false);
    }

    void fixedUpdate() {
        thrust(j.playerShip.getPosition().sub(getPosition()).normalized());
        setRotation(getSpeed().getAngle());

        super.fixedUpdate();
    }
}
