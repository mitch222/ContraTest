package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import objects.Projectile;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] skaterArr;
    private BufferedImage[][] turretArr;
    private ArrayList<Skater> skaters = new ArrayList<>();
    private ArrayList<Turret> turrets = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        skaters = level.getSkates();
        turrets = level.getTurrets();
    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;
        for (Skater c : skaters)
            if (c.isActive()) {
                c.update(lvlData, player);
                isAnyActive = true;
            }
        for (Turret t : turrets)
            if (t.isActive()) {
                t.update(lvlData, player);
                isAnyActive = true;
            }
        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawSkaters(g, xLvlOffset);
        drawTurrets(g, xLvlOffset);
    }

    private void drawSkaters(Graphics g, int xLvlOffset) {
        for (Skater c : skaters) {
            if (c.isActive()) {
                c.drawHitbox(g, xLvlOffset);
                g.drawImage(skaterArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - SKATER_DRAWOFFSET_X + c.flipX() / 2,
                        (int) c.getHitbox().y - SKATER_DRAWOFFSET_Y, SKATER_WIDTH * c.flipW(), SKATER_HEIGHT, null);
                c.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    private void drawTurrets(Graphics g, int xLvlOffset) {
        for (Turret t : turrets) {
            if (t.isActive()) {
                t.drawHitbox(g, xLvlOffset);
                g.drawImage(turretArr[t.getEnemyState()][t.getAniIndex()], (int) t.getHitbox().x - xLvlOffset - TURRET_DRAWOFFSET_X + t.flipX() / 2,
                        (int) t.getHitbox().y - TURRET_DRAWOFFSET_Y, TURRET_WIDTH * t.flipW(), TURRET_HEIGHT, null);
                t.drawProjectiles(g,xLvlOffset);
            }
        }
    }

    public void checkEnemyHit(Projectile bullet) {
        for (Skater c : skaters)
            if (c.isActive())
                if (bullet.getHitbox().intersects(c.getHitbox()) && bullet.isActive()) {
                    bullet.setActive(false);
                    c.hurt(3);
                    return;
                }

        for (Turret c : turrets)
            if (c.isActive())
                if (bullet.getHitbox().intersects(c.getHitbox()) && bullet.isActive()) {
                    bullet.setActive(false);
                    c.hurt(3);
                    return;
                }
    }

    private void loadEnemyImgs() {
        skaterArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SKATER_SPRITE),6,5,SKATER_WIDTH_DEFAULT,SKATER_HEIGHT_DEFAULT);
        turretArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.TURRET_SPRITE),4,5,TURRET_WIDTH_DEFAULT,TURRET_HEIGHT_DEFAULT);
    }

    private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
        BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
        for (int j = 0; j < tempArr.length; j++)
            for (int i = 0; i < tempArr[j].length; i++)
                tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
        return tempArr;
    }

    public void resetAllEnemies() {
        for (Skater c : skaters)
            c.resetEnemy();
        for (Turret c : turrets)
            c.resetEnemy();
    }
}