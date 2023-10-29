package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Checkpoint;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage flagSprite;
    private ArrayList<Flag> checkpoint;
    private Checkpoint lastCheck;
    private Level currentLevel;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        currentLevel = playing.getLevelManager().getCurrentLevel();
        loadImgs();
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Flag p : checkpoint)
            if (p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }

    public void applyEffectToPlayer(Flag p){
        if (p.getObjType() == FLAG) {
            lastCheck = playing.savePoint();
            System.out.println(1);
        }
    }

    public void loadObjects(Level newLevel) {
        currentLevel = newLevel;
        checkpoint = new ArrayList<>(newLevel.getCheckpoints());
    }

    private void loadImgs() {
        flagSprite = LoadSave.GetSpriteAtlas(LoadSave.FLAG_ATLAS);
    }

    public void update() {
        for (Flag p : checkpoint)
            if (p.isActive())
                p.update();
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawFlags(g, xLvlOffset);
    }


    private void drawFlags(Graphics g, int xLvlOffset) {
        for (Flag p : checkpoint)
            if (p.isActive()) {
                g.drawImage(flagSprite, (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), FLAG_WIDTH, FLAG_HEIGHT,
                        null);
            }
    }

    public void restore(){
        lastCheck.restore();
    }

    public void resetAllObjects() {
        for (Flag p : checkpoint)
            p.reset();
    }

}