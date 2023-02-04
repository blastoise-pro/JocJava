package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class Battlecruiser extends EnemyShip {
    private final static int[] xPoints = {-32, -32, -6, 48, 48, -6};
    private final static int[] yPoints = {30, -30, -30, -14, 14, 30};

    Battlecruiser(Joc j, Vec2 position) {
        super(j, position, 30, 20, 100, 0.95f, .7f, 5);
        Vec2 hitboxScale = new Vec2(.2f,.2f);
        sprite = new Sprite(AssetLoader.enemyBattlecruiser, (float) -Math.PI/2, hitboxScale);
        shipShape = AffineTransform.getScaleInstance(hitboxScale.x, hitboxScale.y)
                .createTransformedShape(new Polygon(xPoints, yPoints, xPoints.length));
        updateCollider();
        hpBar = new HPBar(j, this, new Vec2(0, -sprite.getHeight()/2), new Vec2(sprite.getHeight(), 1));
        explosion = new Animation(AssetLoader.explosionBig, new Vec2(0.5f, 0.5f), 1, false);
    }
}
