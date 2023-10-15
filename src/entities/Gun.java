package entities;

import main.Game;
import objects.Projectile;
import utilz.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static utilz.Constants.Projectiles.*;
import static utilz.HelpMethods.*;
import static utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;

public class Gun extends Entity{
    private Image[] position;
    private BufferedImage ammoImg;
    private int gunPos;
    private int actualWidth;
    private int actualHeight;
    private ArrayList<Projectile> ammo = new ArrayList<>();
    private int flipX = 0;
    private int flipW = 1;
    private String dir = "right";

    public Gun(float x, float y, int width, int height) {
        super(x, y, width, height);
        actualWidth = width;
        actualHeight = height;
        loadStatic();
        initHitbox(x,y,width,height);
    }

    public void render(Graphics g, int lvlOffset) {
        //drawHitbox(g, lvlOffset);
        g.drawImage(position[gunPos], (int) (hitbox.x) - lvlOffset + flipX/2, (int) (hitbox.y), actualWidth * flipW, actualHeight, null);
        drawProjectiles(g, lvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : ammo)
            if (p.isActive())
                g.drawImage(ammoImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), BULLET_WIDTH, BULLET_HEIGHT, null);

    }

    public void setArms(String posArms) {

        if (posArms.equals("up")){
            actualWidth = (int) (21 * Game.SCALE);
            actualHeight = (int)(20 * Game.SCALE);
            gunPos = 1;
        }
        else {
            actualWidth = (int) (26 * Game.SCALE);
            actualHeight = (int) (7 * Game.SCALE);
            gunPos = 0;
        }
    }

    public void shootGun() {
        ammo.add(new Projectile((int) this.getHitbox().x, (int) this.getHitbox().y, dir));
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

    public void updatePos(String dir) {
        if (dir.equals("left")) {
            this.dir = dir;
            flipX = actualWidth;
            flipW = -1;
        }
        if (dir.equals("right")) {
            this.dir = dir;
            flipX = 0;
            flipW = 1;
        }

    }

    private void loadStatic() {
        InputStream is = null;
        try {
            Image img;
            position = new Image[2];
            for (int i = 0; i < position.length; i++) {
                is = getClass().getResourceAsStream("/2 Guns/1_" + (1 + i) +".png");
                img = ImageIO.read(is);
                position[i] = img;
            }
            ammoImg = LoadSave.GetSpriteAtlas(LoadSave.BULLET1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Image[] getPosition() {
        return position;
    }

    public void setPosition(Image[] position) {
        this.position = position;
    }

    public int getGunPos() {
        return gunPos;
    }

    public void setGunPos(int gunPos) {
        this.gunPos = gunPos;
    }

    public int getActualWidth() {
        return actualWidth;
    }

    public void setActualWidth(int actualWidth) {
        this.actualWidth = actualWidth;
    }

    public int getActualHeight() {
        return actualHeight;
    }

    public void setActualHeight(int actualHeight) {
        this.actualHeight = actualHeight;
    }

    public ArrayList<Projectile> getAmmo() {
        return ammo;
    }
}
