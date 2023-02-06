package joc1;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

class Upgrade {
    String name;
    String description;
    BufferedImage icon;

    float maxHP = 0;
    float maxSpeedPercent = 1;
    float thrustPowerPercent = 1;
    float attackDamage = 0;
    float attackSpeedPercent = 1;
    int numberShots = 0;
    int bulletPenetration = 0;
    Set<BulletEffect> bulletEffects = new HashSet<>();

    Upgrade(String name, String description, BufferedImage icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    void apply(PlayerShip playerShip) {
        playerShip.maxHP += maxHP;
        playerShip.HP += maxHP;

        playerShip.maxSpeed *= maxSpeedPercent;
        playerShip.thrustPower *= thrustPowerPercent;

        playerShip.attackDamage += attackDamage;
        playerShip.attackSpeed *= attackSpeedPercent;
        playerShip.numberShots += numberShots;
        playerShip.bulletPenetration += bulletPenetration;
        playerShip.bulletEffects.addAll(bulletEffects);
    }
}
