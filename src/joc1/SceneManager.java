package joc1;

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
}
