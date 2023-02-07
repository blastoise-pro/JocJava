package joc1;

import java.awt.image.BufferedImage;
import java.util.EnumSet;
import java.util.Set;

class Upgrade {
    String name;
    String description;
    BufferedImage icon;
    int repeats;

    float maxHP = 0;

    float maxSpeedPercent = 1;
    float thrustPowerPercent = 1;

    float attackDamage = 0;
    float attackSpeedPercent = 1;

    int numberShots = 0;
    int bulletPenetration = 0;
    int bulletBounces = 0;
    float bulletSizePercent = 1;
    Set<BulletEffect> bulletEffects = EnumSet.noneOf(BulletEffect.class);

    Upgrade(String name, String description, BufferedImage icon, int repeats) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.repeats = repeats;
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
        playerShip.bulletBounces += bulletBounces;
        playerShip.bulletSize *= bulletSizePercent;
        playerShip.bulletEffects.addAll(bulletEffects);
        if (repeats > 0) {
            repeats--;
        }
    }
}
