package joc1;

import java.awt.*;
import java.awt.image.BufferedImage;

class LVUPanel extends Panel {
    private final static float speed = 4;
    private final static BufferedImage normalImage = AssetLoader.border1;
    private final static BufferedImage hoverImage = AssetLoader.border2;
    private final static Font fontTitle = new Font("Arial", Font.BOLD, 26);
    private final static Font fontDescription = new Font("Arial", Font.PLAIN, 14);

    Upgrade upgrade;
    LVUPanel(Joc j, Vec2 position, Upgrade upgrade) {
        super(j, position, 0, new Vec2(0.4f, 0.3f), new Vec2(.5f, .5f), normalImage, null);
        this.upgrade = upgrade;
        if (upgrade != null) {
            GUIElement title = new GUIText(j, new Vec2(0.08f, 0.15f), 0, new Vec2(0f, 0f), upgrade.name, Color.white, fontTitle, this);
            new GUIText(j, new Vec2(0f, 1.5f), 0, new Vec2(0f, 0f), upgrade.description, Color.white, fontDescription, title);
        }
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
