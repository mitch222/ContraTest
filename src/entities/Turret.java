package entities;

import main.Game;
import objects.Projectile;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Projectiles.BULLET_HEIGHT;
import static utilz.Constants.Projectiles.BULLET_WIDTH;
import static utilz.HelpMethods.IsProjectileHittingLevel;

public class Turret extends Enemy{
    private BufferedImage ammoImg;
    private ArrayList<Projectile> ammo = new ArrayList<>();
    private int recoil = 60;

    public Turret(float x, float y) {
        super(x, y, TURRET_WIDTH, TURRET_HEIGHT, TURRET);
        ammoImg = LoadSave.GetSpriteAtlas(LoadSave.BULLET1);
        initHitbox(x, y, (int) (12 * Game.SCALE), (int) (26 * Game.SCALE));
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateProjectiles(lvlData);
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        recoil--;
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            checkPlayerHit(null, player);
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (recoil <= 0) {
                        recoil = 60;
                        String dir = "left";
                        if (walkDir == RIGHT)
                            dir = "left";
                        if (walkDir == LEFT)
                            dir = "right";
                        ammo.add(new Projectile((int) this.getHitbox().x, (int) this.getHitbox().y, dir));
                    }
                    break;
                case HIT:
                    break;
            }
        }

    }
    @Override
    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if(!ammo.isEmpty()) {
            for (Projectile bullet: ammo) {
                if (bullet.getHitbox().intersects(player.hitbox) && bullet.isActive()) {
                    player.changeHealth(-GetEnemyDmg(enemyType));
                    bullet.setActive(false);
                }
            }
        }
    }


    public void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : ammo)
            if (p.isActive())
                g.drawImage(ammoImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), BULLET_WIDTH, BULLET_HEIGHT, null);

    }

    public void updateProjectiles(int[][] lvlData) {
        for (Projectile p : ammo)
            if (p.isActive()) {
                p.updatePos();
                if (IsProjectileHittingLevel(p, lvlData)) {
                    p.setActive(false);
                }
            }
    }

    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;

    }
}
