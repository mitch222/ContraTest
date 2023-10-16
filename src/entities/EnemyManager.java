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
    private ArrayList<Skater> skaters = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        skaters = level.getSkates();
    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;
        for (Skater c : skaters)
            if (c.isActive()) {
                c.update(lvlData, player);
                isAnyActive = true;
            }
        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawSkaters(g, xLvlOffset);
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

    public void checkEnemyHit(Projectile bullet) {
        for (Skater c : skaters)
            if (c.isActive())
                if (bullet.getHitbox().intersects(c.getHitbox()) && bullet.isActive()) {
                    bullet.setActive(false);
                    c.hurt(3);
                    return;
                }
    }

    private void loadEnemyImgs() {
        skaterArr = new BufferedImage[5][6];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SKATER_SPRITE);
        for (int j = 0; j < skaterArr.length; j++)
            for (int i = 0; i < skaterArr[j].length; i++)
                skaterArr[j][i] = temp.getSubimage(i * SKATER_WIDTH_DEFAULT, j * SKATER_HEIGHT_DEFAULT, SKATER_WIDTH_DEFAULT, SKATER_HEIGHT_DEFAULT);
    }

    public void resetAllEnemies() {
        for (Skater c : skaters)
            c.resetEnemy();
    }
}