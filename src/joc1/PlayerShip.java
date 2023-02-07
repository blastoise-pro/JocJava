package joc1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

class PlayerShip extends Ship {
    private final static int[] xPoints = {-26, -26, -14, -8, 30, -8, -14};
    private final static int[] yPoints = {36, -36, -36, -24, 0, 24, 36};

    private final static Vec2[] shotSpawnPoints = {
            new Vec2(4f, 0f), new Vec2(1f, 2f), new Vec2(1, -2f),
            new Vec2(-1f, 3.5f), new Vec2(-1f, -3.5f)
    };
    private final static List<List<Vec2>> shotSpawnsByLevel = new ArrayList<>();

    private final static String label = "player";
    private final static Set<String> collisionMask = new HashSet<>(List.of("bulletF"));

    Sprite sprite;
    Animation explosion;
    HPBar hpBar;
    double lastDamageTime = 0;
    double invTime = 0.3f;

    int level = 1;
    int totalXP = 0;
    int lastXPLevel = 0;
    int nextXPlevel = 20;

    float attackDamage = 3;
    int bulletPenetration = 1;
    int bulletBounces = 0;
    float bulletSize = 1;
    Set<BulletEffect> bulletEffects = EnumSet.noneOf(BulletEffect.class);
    int numberShots = 1;

    PlayerShip(Joc j, Vec2 position) {
        super(j, position, 0, new Vec2(1f, 1f), new Vec2(), 10,
                40, 100, 0.03f, 0.9f, 0f,
                200, 1f, null);

        shotSpawnsByLevel.add(List.of(shotSpawnPoints[0]));
        shotSpawnsByLevel.add(List.of(shotSpawnPoints[1], shotSpawnPoints[2]));
        shotSpawnsByLevel.add(List.of(shotSpawnPoints[0], shotSpawnPoints[1], shotSpawnPoints[2]));
        shotSpawnsByLevel.add(List.of(shotSpawnPoints[0], shotSpawnPoints[1], shotSpawnPoints[2], shotSpawnPoints[3], shotSpawnPoints[4]));

        Vec2 hitboxScale = new Vec2(0.1f, 0.1f);
        sprite = new Sprite(AssetLoader.playerShip1, hitboxScale);
        shipShape = AffineTransform.getScaleInstance(hitboxScale.x, hitboxScale.y)
                .createTransformedShape(new Polygon(xPoints, yPoints, xPoints.length));
        updateCollider();
        hpBar = new HPBar(j, this, new Vec2(0, -sprite.getHeight()/2 - 2), new Vec2(sprite.getHeight(), 1));
        explosion = new Animation(AssetLoader.explosionHuge, new Vec2(.5f, .5f), 1.5, false);
    }

    void update() {
        if (j.gameController.gamePaused) {
            return;
        }

        if (isDead) {
            if (explosion.getIndex() == 33) {
                drawShip = false;
            }
            if (!explosion.isPlaying()) {
                j.gameController.togglePause();
            }
            return;
        }

        if (totalXP >= nextXPlevel) {
            levelUp();
        }

        if (Input.getAction(Action.SHOOT) && (Time.time() - lastShotTime >= 1/attackSpeed)) {
            shoot();
        }
    }

    void fixedUpdate() {
        if (isDead) {
            return;
        }
        setRotation(Input.getMousePosition().sub(getPosition()).getAngle());
        thrust(Input.getDirection());

        super.fixedUpdate();
    }

    void shoot() {
        lastShotTime = Time.time();
        Vec2 shipPos = getPosition();
        for (Vec2 shotSpawnPos:shotSpawnsByLevel.get(numberShots - 1)) {
            new BasicBullet(j, shipPos.add(shotSpawnPos.rotate(getRotation())), Vec2.unitFromAngle(getRotation()).scale(bulletSpeed),
                    bulletSize, true, attackDamage, bulletPenetration, bulletBounces, bulletEffects);
        }
    }

    @Override
    public void onColliderEnter(Collider other) {
        if (other.getLabel().equals("enemy")) {
            EnemyShip enemy = (EnemyShip) other;
            collideWithShip(enemy);
            if (Time.time() > lastDamageTime + invTime) {
                lastDamageTime = Time.time();
                damage(enemy.contactDamage);
            }
        }
        else if (other.getLabel().equals("bulletE")) {
            Bullet bullet = (Bullet) other;
            damage(bullet.damage);
        }
    }

    void damage(float amount) {
        System.out.println("Damage");
        HP -= amount;
        if (HP <= 0 && !isDead) {
            HP = 0;
            isDead = true;
            explosion.play();
            j.sceneManager.showGameOver();
        }
    }

    void addXP(int val) {
        totalXP += val;
    }

    void levelUp() {
        lastXPLevel = nextXPlevel;
        nextXPlevel = (int) (lastXPLevel + 10 * (30 - 30 * Math.exp(-0.1 * level) + level * 0.01));
        level++;
        j.gameController.levelUp();
    }

    @Override
    public void onColliderStay(Collider other) {

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

        if (drawShip){
            sprite.pintar(g, PVM);
        }

        if (Joc.DRAW_HITBOXES) {
            Shape transPoly = PVM.createTransformedShape(shipShape);
            g.fill(transPoly);
        }

        if (Joc.DRAW_BULLETSPAWNS) {
            Sprite indicator = new Sprite(AssetLoader.gem1, new Vec2(0.1f, 0.1f));
            for(Vec2 spawn:shotSpawnPoints) {
                AffineTransform spawnPos = new AffineTransform(PVM);
                spawnPos.translate(spawn.x, spawn.y);
                indicator.pintar(g, spawnPos);
            }
        }

        if (explosion.isPlaying()) {
            explosion.getFrame().pintar(g, PVM);
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
