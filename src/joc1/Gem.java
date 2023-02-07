package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Gem extends PhysicsObject {
    private final static float magnetRange = 25;
    private final static float linearRange = 10;
    private final static float collectRange = 5;
    private final static float initialForce = 100;
    private final static Sprite sprite = new Sprite(AssetLoader.gem1, new Vec2(0.3f, 0.3f));

    int XP;
    private boolean magnetActive = false;
    private boolean linearActive = false;
    Gem(Joc j, Vec2 position, int XP) {
        super(j, position, 0, new Vec2(1, 1), new Vec2());
        this.XP = XP;
    }

    void fixedUpdate() {
        Vec2 toPlayerVec = j.playerShip.getPosition().sub(getPosition());
        float distToPlayer = toPlayerVec.norm();
        if (distToPlayer < collectRange) {
            j.playerShip.addXP(XP);
            j.destroy(this);
        }
        if (magnetActive) {
            applyForce(toPlayerVec.scale(50/(distToPlayer*distToPlayer)));
            translate(getSpeed().scale((float) Time.deltaTime()));
            if (distToPlayer < linearRange) {
                magnetActive = false;
                linearActive = true;
            }
        }
        else if (linearActive) {
            translate(toPlayerVec.scale((float) Time.deltaTime() / distToPlayer * getSpeed().norm()));
        }
        else if (distToPlayer <= magnetRange) {
            magnetActive = true;
            applyForce(toPlayerVec.scale(-initialForce/distToPlayer));
        }


    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PVMatrix) {
        AffineTransform PVM = new AffineTransform(PVMatrix);
        PVM.concatenate(getModelMatrix());

        sprite.pintar(g, PVM);
    }
}
