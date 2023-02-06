package joc1;

import java.awt.image.BufferedImage;

class LVUPanel extends Panel {
    private final static float speed = 4;
    private final static BufferedImage normalImage = AssetLoader.border1;
    private final static BufferedImage hoverImage = AssetLoader.border2;


    Upgrade upgrade;
    LVUPanel(Joc j, Vec2 position, Upgrade upgrade) {
        super(j, position, 0, new Vec2(0.4f, 0.3f), new Vec2(.5f, .5f), normalImage, null);
        this.upgrade = upgrade;
    }

    void update() {
        if (getPosition().x > 0.5f) {
            translate(new Vec2(-speed * (float) Time.unscaledDeltaTime(), 0));
        }
        Vec2 pos = getPosition();
        if (pos.x < 0.5f) {
            setPosition(new Vec2(0.5f, pos.y));
        }
    }

    @Override
    public void onMouseClick() {
        if (upgrade != null){
            j.gameController.upgradePicked(upgrade);
        }
    }

    @Override
    public void onMouseEnter() {
        if (upgrade != null) {
            image = hoverImage;
        }
    }

    @Override
    public void onMouseExit() {
        image = normalImage;
    }
}
