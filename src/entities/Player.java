package entities;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

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
    private int flipX = 0;
    private int flipW = 1;

    // Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    //Brazos Y arma

    private BufferedImage[] arms;
    private int armPos = ARMRIGTH;
    private float posArmX;
    private float posAmrY;

    private Gun gun;
    private int recoil = 65;

    // StatusBarUI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 10;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;
    private Playing playing;


    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        loadStatic();
        initHitbox(x, y, (int)(12 * Game.SCALE), (int)(26 * Game.SCALE));
        this.posArmX = (hitbox.x + 6);
        this.posAmrY = (hitbox.y + 18);
        gun = new Gun(((hitbox.x - xDrawOffset) + 10), (int) ((hitbox.y - yDrawOffset) + 37), (int)(26 * Game.SCALE), (int)(7 * Game.SCALE));

    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    public void update() {
        updateHealthBar();

        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }
        if(recoil>0)
            recoil--;
        updatePos();
        updateAnimationTick();
        setAnimation();
        setArms();
        gun.updateProjectiles(lvlData);
        checkAttack();
    }

    public void render(Graphics g,  int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX/2,
                (int) (hitbox.y - yDrawOffset), width * flipW, height, null);
        //drawHitbox(g, lvlOffset);
        g.drawImage(arms[armPos], (int) (posArmX - xDrawOffset) - lvlOffset + flipX/4,
                (int) (posAmrY - yDrawOffset), (int) (32 * Game.SCALE) * flipW, (int) (32 * Game.SCALE), null);
        gun.render(g, lvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
            }
        }
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void checkAttack() {
        if(!gun.getAmmo().isEmpty()) {
            for (int i = 0; i < gun.getAmmo().size(); i++) {
                playing.checkEnemyHit(gun.getAmmo().get(i));
            }
        }
    }


    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            if(attacking && recoil == 0){
                playerAction = RUNNING;
                gun.shootGun();
                recoil = 65;
            }
            else
                playerAction = RUNNING;
        }
        else{
            if (attacking && recoil == 0){
                playerAction = IDLE;
                gun.shootGun();
                recoil = 65;
            }
            playerAction = IDLE;
        }

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

        if (left) {
            xSpeed -= playerSpeed;
            flipX = width;
            flipW = -1;
            gun.updatePos("left");
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
            gun.updatePos("right");
        }

        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                posAmrY = (int) (hitbox.y + 18);
                gun.hitbox.y = (int) ((hitbox.y - yDrawOffset) + 37);
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
            posArmX = (int) (hitbox.x  + 6);
            gun.hitbox.x = (int) ((hitbox.x - xDrawOffset) + 10);
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }

    }
    public void changeHealth(int value) {
        currentHealth += value;

        if (currentHealth <= 0)
            currentHealth = 0;
        else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
            animations = new BufferedImage[10][6];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 48, j * 48, 48, 48);
    }

    private void loadStatic() {
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
        InputStream is = null;
        try {
            BufferedImage img;
            arms = new BufferedImage[3];
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

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

}