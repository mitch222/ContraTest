package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] skaterArr;
    private ArrayList<Skater> skaters = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        skaters = LoadSave.GetSkaters();
        System.out.println("size of crabs: " + skaters.size());
    }

    public void update(int[][] lvlData, Player player) {
        for (Skater c : skaters)
            c.update(lvlData, player);
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawSkaters(g, xLvlOffset);
    }

    private void drawSkaters(Graphics g, int xLvlOffset) {
        for (Skater c : skaters) {
            c.drawHitbox(g,xLvlOffset);
            g.drawImage(skaterArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - SKATER_DRAWOFFSET_X + c.flipX()/2,
                    (int) c.getHitbox().y - SKATER_DRAWOFFSET_Y, SKATER_WIDTH * c.flipW(), SKATER_HEIGHT, null);
        }
    }

    private void loadEnemyImgs() {
        skaterArr = new BufferedImage[5][6];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SKATER_SPRITE);
        for (int j = 0; j < skaterArr.length; j++)
            for (int i = 0; i < skaterArr[j].length; i++)
                skaterArr[j][i] = temp.getSubimage(i * SKATER_WIDTH_DEFAULT, j * SKATER_HEIGHT_DEFAULT, SKATER_WIDTH_DEFAULT, SKATER_HEIGHT_DEFAULT);
    }
}