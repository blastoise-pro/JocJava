package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

class PlayerShip extends Ship {
    private final static int[] xPoints = {-26, -26, -14, -8, 30, -8, -14};
    private final static int[] yPoints = {36, -36, -36, -24, 0, 24, 36};

    private final static String label = "player";
    private final static Set<String> collisionMask = new HashSet<>(List.of("bulletF"));

    Sprite sprite;

    PlayerShip(Joc j, Vec2 position) {
        super(j, position, 0, new Vec2(1f, 1f), new Vec2(), 40, 100, 0.9f, Direction.RIGHT, new Vec2(),
                Direction.RIGHT.vector(), 2, 100, 1f,
                new Polygon(xPoints, yPoints, xPoints.length));
        sprite = new Sprite("assets/Ships/basic1.png", new Vec2(0.1f, 0.1f));
    }

    void update() {
        pointCannonAt(Input.getMousePosition());
        if (Input.getAction(Action.SHOOT) && (Time.time() - lastShotTime >= 1/attackSpeed)) {
            shoot();
            System.out.println("Clicked at: " + Input.getMousePosition());
        }
    }

    void fixedUpdate() {
        setRotation(Input.getMousePosition().sub(getPosition()).getAngle());
        thrust(Input.getDirection());

        super.fixedUpdate();
    }

    void shoot() {
        lastShotTime = Time.time();
        new BasicBullet(j, getCannonPos(), getCannonDir().scale(bulletSpeed), true);
    }

    @Override
    public void onColliderEnter(Collider other) {
        if (other.getLabel().equals("enemy") || other.getLabel().equals("bulletE")) {
            //System.out.println("Damage");
            //j.destroy(this);
        }
    }

    @Override
    public void onColliderExit(Collider other) {

    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Set<String> getCollisionMask() {
        return collisionMask;
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        g.setColor(Color.cyan);
        AffineTransform PVM = new AffineTransform(PVMatrix);
        PVM.concatenate(getModelMatrix());

        sprite.pintar(g, PVM);

        if (Joc.DRAW_HITBOXES) {
            Shape transPoly = PVM.createTransformedShape(shipShape);
            g.fill(transPoly);
        }
        /*
        Vec2 cannonDR = getCannonDir().scale(cannonLength);
        Vec2 cannonPos = getCannonPos();
        float[] cannonPoints = {cannonPos.x, cannonPos.y, cannonPos.x + cannonDR.x, cannonPos.y + cannonDR.y};
        PVMatrix.transform(cannonPoints, 0, cannonPoints, 0, cannonPoints.length/2);
        g.drawLine((int) cannonPoints[0], (int) cannonPoints[1], (int) cannonPoints[2], (int) cannonPoints[3]);
         */
    }
}
