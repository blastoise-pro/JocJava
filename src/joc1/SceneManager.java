package joc1;

import java.awt.*;

enum Scene {
    MAINMENU, GAMEPLAY, LEVELUP
}

class SceneManager {
    private final Joc j;
    Scene current;
    SceneManager(Joc j, Scene current) {
        this.j = j;
        this.current = current;
    }

    GUIElement menuBG;
    GUIElement startButton;
    void showMainMenu() {
        current = Scene.MAINMENU;
        menuBG = new Panel(j, new Vec2(0f, 0f), 0, new Vec2(1f, 1f), new Vec2(0f, 0f), AssetLoader.menuBG, null);
        startButton = new StartButton(j, new Vec2(0.5f, 0.7f), 0, new Vec2(0.2f, 0.2f), new Vec2(0.5f, 0.5f), null);
    }

    void hideMainMenu() {
        j.destroy(menuBG);
        j.destroy(startButton);
    }

    void showGame() {
        current = Scene.GAMEPLAY;
        j.gameController = new GameController(j);
        new GUITimer(j, new Vec2(0.5f, 0.03f), new Vec2(0.5f, 0f));
        new XPBar(j, new Vec2(0.95f, 0.9f), (float) -Math.PI/2, new Vec2(0.7f, 0.05f), new Vec2(0f, 1f), null);
        new LVText(j, new Vec2(0.927f, 0.95f), (float) 0, new Vec2(0.5f, 1f), Color.white, new Font("Arial", Font.PLAIN, 30), null);
        new Background(j);
        j.playerShip = new PlayerShip(j, new Vec2(0, 0));
    }

    GUIElement option1;
    GUIElement option2;
    GUIElement option3;
    void showLevelUpMenu(Upgrade[] options) {
        current = Scene.LEVELUP;
        option1 = new LVUPanel(j, new Vec2(1.4f, 0.2f), options[0]);
        option2 = new LVUPanel(j, new Vec2(1.6f, 0.5f), options[1]);
        option3 = new LVUPanel(j, new Vec2(1.8f, 0.8f), options[2]);
    }

    void hideLevelUpMenu() {
        current = Scene.GAMEPLAY;
        j.destroy(option1);
        j.destroy(option2);
        j.destroy(option3);
    }

    void showGameOver() {
        new GUIText(j, new Vec2(0.5f, 0.3f), 0, new Vec2(0.5f, 0.5f),
                "GAME OVER!",
                Color.white, new Font("Arial", Font.BOLD, 40), null);
        new GUIText(j, new Vec2(0.5f, 0.5f), 0, new Vec2(0.5f, 0.5f),
                "Has aguantat " + Time.getFormattedTime(Time.time()-j.gameController.startTime),
                Color.white, new Font("Arial", Font.PLAIN, 30), null);
    }

    void showWin() {
        new GUIText(j, new Vec2(0.5f, 0.3f), 0, new Vec2(0.5f, 0.5f),
                "Has sobreviscut!",
                Color.white, new Font("Arial", Font.BOLD, 40), null);
        new GUIText(j, new Vec2(0.5f, 0.5f), 0, new Vec2(0.5f, 0.5f),
                "Enhorabona",
                Color.white, new Font("Arial", Font.PLAIN, 30), null);
    }
}
