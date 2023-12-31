package objects;

import java.awt.geom.Rectangle2D;

import main.Game;

import static utilz.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private String dir;
    private boolean active = true;

    public Projectile(int x, int y, String dir) {
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (5 * Game.SCALE);

        if (dir.equals("right"))
            xOffset = (int) (29 * Game.SCALE);

        if (dir.equals("left"))
            xOffset = (int) (-29 * Game.SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, BULLET_WIDTH, BULLET_HEIGHT);
        this.dir = dir;
    }

    public void updatePos() {
        if (dir.equals("left")) {
            hitbox.x += -(SPEED);
        }
        if (dir.equals("right")) {
            hitbox.x += SPEED;
        }
    }

    public void setPos(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

}