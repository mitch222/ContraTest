package gamestates;

import entities.EnemyManager;
import entities.Player;
import objects.ObjectManager;

public class Checkpoint {

    private Playing playing;
    private EnemyManager enemyManager;
    private Player player;
    private ObjectManager objectManager;

    public Checkpoint(Playing playing, EnemyManager enemyManager, Player player, ObjectManager objectManager) {
        this.playing = playing;
        this.enemyManager = enemyManager;
        this.player = new Player(player);
        this.objectManager = objectManager;
    }

    public void restore(){
        playing.setEnemyManager(this.enemyManager);
        playing.setPlayer(this.player);
        playing.setObjectManager(this.objectManager);
    }
}
