package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GameController extends GameObject {
    boolean gamePaused = false;

    List<Upgrade> upgradeList = new ArrayList<>();
    Upgrade placeHolder;
    GameController(Joc j) {
        super(j, new Vec2(), 0, new Vec2(1, 1));
        createUpgrades();
    }

    void update() {
        if (Input.getActionDown(Action.SPECIAL)) {
            j.sceneManager.hideLevelUpMenu();
            togglePause();
        }
        if (Input.getActionDown(Action.PAUSE)) {
            togglePause();
        }
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
        j.sceneManager.showLevelUpMenu(options);
        togglePause();
    }

    void upgradePicked(Upgrade upgrade) {
        upgrade.apply(j.playerShip);
    }

    void createUpgrades() {
        Upgrade up = new Upgrade(
                "Pluja de bales",
                "Velocitat d'atac +25%\n+1 bala",
            AssetLoader.iconSkillAS
        );
        up.attackSpeedPercent = 1.25f;
        up.numberShots = 1;
        upgradeList.add(up);

        up = new Upgrade(
                "Bales de goma",
                "Les bales reboten entre 3 enemics",
                AssetLoader.iconSkillBounce
        );
        up.bulletEffects.add(BulletEffect.BOUNCE);
        upgradeList.add(up);

        up = new Upgrade(
                "Franctirador",
                "+5 de dany\nLes bales travessen 1 enemic més",
                AssetLoader.iconSkillCrosshair
        );
        up.attackDamage = 5;
        up.bulletPenetration = 1;
        upgradeList.add(up);

        up = new Upgrade(
                "Llançamíssils",
                "+5 de dany\nLes bales exploten al xocar",
                AssetLoader.iconSkillExplosion
        );
        up.attackDamage = 5;
        up.bulletEffects.add(BulletEffect.AREA);
        upgradeList.add(up);

        up = new Upgrade(
                "Sistema de control",
                "+10 de vida màxima\nLes bales busquen als enemics automàticament",
                AssetLoader.iconSkillHoming
        );
        up.maxHP = 20;
        up.bulletEffects.add(BulletEffect.HOMING);
        upgradeList.add(up);

        up = new Upgrade(
                "Manteniment",
                "+30 de vida màxima",
                AssetLoader.iconSkillHP
        );
        up.maxHP = 30;
        upgradeList.add(up);

        up = new Upgrade(
                "Motors millorats",
                "+20% de velocitat de moviment",
                AssetLoader.iconSkillMS
        );
        up.maxSpeedPercent = 1.2f;
        up.thrustPowerPercent = 1.2f;
        upgradeList.add(up);

        up = new Upgrade(
                "Reacció en cadena",
                "Els enemics morts exploten alliberant més bales",
                AssetLoader.iconSkillSkull
        );
        up.bulletEffects.add(BulletEffect.REPLICATE);
        upgradeList.add(up);

        placeHolder = new Upgrade(
                "Reparació",
                "+10 de vida màxima",
                AssetLoader.iconSkillHP
        );
        placeHolder.maxHP = 10;
    }

    @Override
    public void pintar(Graphics2D g, AffineTransform PVMatrix) {

    }
}
