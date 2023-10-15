package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;

public class Skater extends Enemy{
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Skater(float x, float y) {
        super(x, y, SKATER_WIDTH, SKATER_HEIGHT, SKATER);
        initHitbox(x, y, (int) (12 * Game.SCALE), (int) (26 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (25 * Game.SCALE), (int) (19 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 20);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player))
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK);

                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;

                    // Changed the name for checkEnemyHit to checkPlayerHit
                    if (aniIndex == 3 && !attackChecked)
                        checkPlayerHit(attackBox, player);

                    break;
                case HIT:
                    break;
            }
        }

    }

    private void updateAttackBox() {
        if(walkDir == RIGHT) {
            attackBox.x = hitbox.x - attackBoxOffsetX ;
        } else if (walkDir == LEFT) {
            attackBox.x = hitbox.x + (int)(attackBoxOffsetX/4);
        }
        attackBox.y = hitbox.y;
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
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
