package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class Scout extends EnemyShip {
    private final static int[] xPoints = {-13, 8, 8, -13};
    private final static int[] yPoints = {-12, -12, 12, 12};
    static final float baseHP = 6;

    Scout(Joc j, Vec2 position) {
        super(j, position, baseHP, 40, 100, 0.9f, 2f, 2);
        Vec2 hitboxScale = new Vec2(0.3f,0.3f);
        sprite = new Sprite(AssetLoader.enemyScout, (float) -Math.PI/2, hitboxScale);
        shipShape = AffineTransform.getScaleInstance(hitboxScale.x, hitboxScale.y)
                .createTransformedShape(new Polygon(xPoints, yPoints, xPoints.length));
        updateCollider();
        hpBar = new HPBar(j, this, new Vec2(0, -sprite.getHeight()/2), new Vec2(sprite.getHeight(), 1));
        explosion = new Animation(AssetLoader.explosionSmall, new Vec2(0.3f, 0.3f), 0.7f, false);
    }

    void fixedUpdate() {
        thrust(j.playerShip.getPosition().sub(getPosition()).normalized());
        setRotation(getSpeed().getAngle());

        super.fixedUpdate();
    }
}
