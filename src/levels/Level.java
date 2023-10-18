package levels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Skater;
import entities.Turret;
import main.Game;

import static utilz.Constants.EnemyConstants.*;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Skater> skaters = new ArrayList<>();
    private ArrayList<Turret> turrets = new ArrayList<>();
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        lvlData = new int[img.getHeight()][img.getWidth()];
        loadLevel();
        calcLvlOffsets();
    }


    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }
    private void loadLevel() {

        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
            }
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 81)
            lvlData[y][x] = 0;
        else
            lvlData[y][x] = redValue;
    }


    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case SKATER -> skaters.add(new Skater(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case TURRET -> turrets.add(new Turret(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case 90 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
        }
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Skater> getSkates() {
        return skaters;
    }
    public ArrayList<Turret> getTurrets() {
        return turrets;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

}