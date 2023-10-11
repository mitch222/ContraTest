package entities;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static utilz.Constants.PlayerConstants.ARMRIGTH;
import static utilz.Constants.PlayerConstants.ARMUP;

public class Gun extends Entity{
    private Image[] position;
    private int gunPos;
    private int actualWidth;
    private int actualHeight;

    public Gun(float x, float y, int width, int height) {
        super(x, y, width, height);
        actualWidth = width;
        actualHeight = height;
        loadStatic();
        initHitbox(x,y,1,1);
    }

    public void render(Graphics g) {
        g.drawImage(position[gunPos], (int) (hitbox.x), (int) (hitbox.y), (int)(actualWidth*Game.SCALE), (int)(actualHeight*Game.SCALE), null);
    }

    public void setArms(String posArms) {

        if (posArms.equals("up")){
            actualWidth = 21;
            actualHeight = 20;
            gunPos = 1;
        }
        else {
            actualWidth = 26;
            actualHeight = 7;
            gunPos = 0;
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
}
