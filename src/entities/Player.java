package entities;

import main.Game;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 0.9f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 7 * Game.SCALE;
    private float yDrawOffset = 21 * Game.SCALE;

    // Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    //Brazos Y arma

    private Image[] arms;
    private int armPos = ARMRIGTH;
    private Gun gun;


    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        loadStatic();
        initHitbox(x, y, (int)(12 * Game.SCALE), (int)(26 * Game.SCALE));
        gun = new Gun(((hitbox.x - xDrawOffset) + 10), (int) ((hitbox.y - yDrawOffset) + 37), 26, 7);

    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
        setArms();
    }

    public void render(Graphics g,  int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
        drawHitbox(g, lvlOffset);
        g.drawImage(arms[armPos], (int) ((hitbox.x - xDrawOffset) + 6) - lvlOffset, (int) ((hitbox.y - yDrawOffset) + 18), (int) (32 * Game.SCALE), (int) (32 * Game.SCALE), null);
        gun.render(g, lvlOffset);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        /*if (attacking)
            playerAction = ATTACK_1;*/
        if (inAir) {
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = IDLE;
        }

        if (startAni != playerAction)
            resetAniTick();
    }

    private void setArms() {
        int startPos = armPos;

        if (up) {
            armPos = ARMUP;
            gun.setArms("up");
        }
        else {
            armPos = ARMRIGTH;
            gun.setArms("right");
        }

    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;

        if (jump)
            jump();

        if (!inAir)
            if ((!left && !right) || (right && left))
                return;

        float xSpeed = 0;

        if (left)
            xSpeed -= playerSpeed;
        if (right)
            xSpeed += playerSpeed;

        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                gun.hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }

        } else
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;

    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;

    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
            gun.hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }

    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
    private void loadAnimations() {
        InputStream is = getClass().getResourceAsStream("/PSD/Biker.png");
        try {
            BufferedImage img = ImageIO.read(is);

            animations = new BufferedImage[10][6];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 48, j * 48, 48, 48);

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

    private void loadStatic() {
        InputStream is = null;
        try {
            Image img;
            arms = new Image[3];
            for (int i = 0; i < arms.length; i++) {
                is = getClass().getResourceAsStream("/3 Hands/1 Biker/" + (8 + i) +".png");
                img = ImageIO.read(is);
                arms[i] = img;
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

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

}