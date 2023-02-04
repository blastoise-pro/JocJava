package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

class Bomber extends EnemyShip {
    private final static int[] xPoints = {-11, -11, 12, 12};
    private final static int[] yPoints = {15, -15, -4, 5};

    float orbitDistance;
    float randomDir;

    Bomber(Joc j, Vec2 position) {
        super(j, position, 9, 50, 10, 0.2f, 2f, 2);
        Vec2 hitboxScale = new Vec2(.2f,.2f);
        sprite = new Sprite(AssetLoader.enemyBomber, (float) -Math.PI/2, hitboxScale);
        shipShape = AffineTransform.getScaleInstance(hitboxScale.x, hitboxScale.y)
                .createTransformedShape(new Polygon(xPoints, yPoints, xPoints.length));
        updateCollider();
        hpBar = new HPBar(j, this, new Vec2(0, -sprite.getHeight()/2), new Vec2(sprite.getHeight(), 1));
        explosion = new Animation(AssetLoader.explosionSmall, new Vec2(0.5f, 0.5f), 0.7f, false);
    }

    void start() {
        orbitDistance = 30; //j.ran.nextFloat(30);
        randomDir = j.ran.nextInt(2);
        randomDir = randomDir == 0 ? -1:1;
    }

    void fixedUpdate() {
        Vec2 toPlayerVec = j.playerShip.getPosition().sub(getPosition());
        float distance = toPlayerVec.norm();
        float dToOrbit = distance - orbitDistance;
        thrust(toPlayerVec.scale(dToOrbit/distance * 0.5f));

        thrust(new Vec2(toPlayerVec.y, -toPlayerVec.x).scale( 1/(1 + 0.1f*dToOrbit*dToOrbit) * randomDir));

        setRotation(getSpeed().getAngle());

        super.fixedUpdate();
    }
}
