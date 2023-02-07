package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GameController extends GameObject {
    double startTime;
    boolean gamePaused = false;

    int currentMinute = 0;
    double lastWaveTime = 0;
    double waveInterval = 5;
    float waveSizeScaling = 0.5f;
    float waveSize = 3;
    List<List<Class<? extends EnemyShip>>> enemyPoolByMinute = new ArrayList<>();
    int enemyLimit = 300;

    List<Upgrade> upgradeList = new ArrayList<>();
    Upgrade placeHolder;

    final List<EnemyShip> enemyList = new ArrayList<>();
    GameController(Joc j) {
        super(j, new Vec2(), 0, new Vec2(1, 1));
        createUpgrades();
        enemyPoolByMinute.add(List.of(Scout.class));
        enemyPoolByMinute.add(List.of(Fighter.class, Scout.class));
        enemyPoolByMinute.add(List.of(Scout.class, Frigate.class));
        enemyPoolByMinute.add(List.of(Fighter.class, Scout.class, Frigate.class));
        enemyPoolByMinute.add(List.of(Fighter.class, Scout.class, Frigate.class, Bomber.class));
    }

    void start() {
        startTime = Time.time();
        newWave();
        //enemyList.add(new Battlecruiser(j, new Vec2(100, 0)));
        //enemyList.add(new Battlecruiser(j, new Vec2(100, 20)));
        //enemyList.add(new Battlecruiser(j, new Vec2(100, -20)));
    }

    void update() {
        if (Input.getActionDown(Action.SHOOT)){
            System.out.println("Clicked at: " + Input.getMousePosition());
        }
        int lastMinute = currentMinute;
        currentMinute = (int) ((Time.time() - startTime)/60);
        if (currentMinute == 5 && j.sceneManager.current == Scene.GAMEPLAY) {
            j.sceneManager.showWin();
            togglePause();
        }
        if (lastMinute != currentMinute) {
            enemyList.add(new Battlecruiser(j, j.playerShip.getPosition().add(Vec2.randomWithRadius(300))));
        }

        /*
        if (Input.getActionDown(Action.SPECIAL)) {
            if (j.sceneManager.current == Scene.LEVELUP){
                j.sceneManager.hideLevelUpMenu();
                togglePause();
            }
            else {
                levelUp();
            }
        }
        */

        if (Input.getActionDown(Action.PAUSE) && j.sceneManager.current == Scene.GAMEPLAY) {
            togglePause();
        }

        if (gamePaused) {
            return;
        }

        if (Time.time() - lastWaveTime >= waveInterval) {
            newWave();
        }
        /*
        for (int i = 0; i < 5; i++) {
            new Bomber(j, Vec2.randomWithRadius(200));
            new Frigate(j, Vec2.randomWithRadius(200));
        }
        new Battlecruiser(j, new Vec2(100, 0));
        new Fighter(j, new Vec2(0, 200));
        new Scout(j, new Vec2());

         */
    }

    void newWave() {
        lastWaveTime = Time.time();
        List<Class<? extends EnemyShip>> options;
        if (currentMinute < enemyPoolByMinute.size()) {
             options = enemyPoolByMinute.get(currentMinute);
        }
        else {
            options = enemyPoolByMinute.get(enemyPoolByMinute.size()-1);
        }
        for (int i = 0; i < (int) waveSize; i++) {
            if (enemyList.size() >= enemyLimit) break;
            Class<? extends EnemyShip> choice = options.get(j.ran.nextInt(options.size()));
            try {
                enemyList.add(choice.getDeclaredConstructor(Joc.class, Vec2.class).newInstance(j, j.playerShip.getPosition().add(Vec2.randomWithRadius(300))));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        waveSize += waveSizeScaling;
        System.out.println("EnemyList size: " + enemyList.size());
    }

    void togglePause() {
        if (!gamePaused){
            gamePaused = true;
            Time.timeScale = 0;
        }
        else {
            gamePaused = false;
            Time.timeScale = 1;
        }
    }

    void levelUp() {
        Collections.shuffle(upgradeList);
        Upgrade[] options = new Upgrade[3];
        int i = 0;
        for (Upgrade up:upgradeList) {
            options[i++] = up;
            if (i >= 3) {
                break;
            }
        }
        if (options[0] == null) {
            options[0] = placeHolder;
        }
        j.sceneManager.showLevelUpMenu(options);
        togglePause();
    }

    void upgradePicked(Upgrade upgrade) {
        upgrade.apply(j.playerShip);
        if (upgrade.repeats == 0) {
            upgradeList.remove(upgrade);
        }
        j.sceneManager.hideLevelUpMenu();
        togglePause();
    }

    void createUpgrades() {
        Upgrade up = new Upgrade(
                "Pluja de bales",
                "+1 bala",
            AssetLoader.iconSkillAS, 3
        );
        up.numberShots = 1;
        upgradeList.add(up);

        up = new Upgrade(
                "Sobrecàrrega",
                "Velocitat d'atac +15%",
                AssetLoader.iconSkillAS, -1
        );
        up.attackSpeedPercent = 1.15f;
        upgradeList.add(up);

        up = new Upgrade(
                "Bales de goma",
                "Les bales reboten entre 3 enemics\nTamany de les bales +50%",
                AssetLoader.iconSkillBounce, 1
        );
        up.bulletEffects.add(BulletEffect.BOUNCE);
        up.bulletSizePercent = 1.5f;
        up.bulletBounces = 3;
        upgradeList.add(up);

        up = new Upgrade(
                "Franctirador",
                "+2 de dany\nLes bales travessen 1 enemic més",
                AssetLoader.iconSkillCrosshair, -1
        );
        up.attackDamage = 2;
        up.bulletPenetration = 1;
        upgradeList.add(up);

        up = new Upgrade(
                "Llançamíssils",
                "+2 de dany\nLes bales exploten al xocar",
                AssetLoader.iconSkillExplosion, 1
        );
        up.attackDamage = 2;
        up.bulletEffects.add(BulletEffect.AREA);
        upgradeList.add(up);

        up = new Upgrade(
                "Sistema de control",
                "+10 de vida màxima\nLes bales busquen als enemics automàticament",
                AssetLoader.iconSkillHoming, 1
        );
        up.maxHP = 10;
        up.bulletEffects.add(BulletEffect.HOMING);
        upgradeList.add(up);

        up = new Upgrade(
                "Manteniment",
                "+30 de vida màxima",
                AssetLoader.iconSkillHP, -1
        );
        up.maxHP = 30;
        upgradeList.add(up);

        up = new Upgrade(
                "Motors millorats",
                "+25% de velocitat de moviment",
                AssetLoader.iconSkillMS, -1
        );
        up.maxSpeedPercent = 1.25f;
        up.thrustPowerPercent = 1.25f;
        upgradeList.add(up);

        up = new Upgrade(
                "Reacció en cadena",
                "Els enemics morts exploten alliberant més bales",
                AssetLoader.iconSkillSkull, 1
        );
        up.bulletEffects.add(BulletEffect.REPLICATE);
        upgradeList.add(up);

        placeHolder = new Upgrade(
                "Reparació",
                "+10 de vida màxima",
                AssetLoader.iconSkillHP, -1
        );
        placeHolder.maxHP = 10;
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PVMatrix) {

    }
}
