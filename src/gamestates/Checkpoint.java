package gamestates;

import entities.EnemyManager;
import entities.Player;

public class Checkpoint {

    private Playing playing;
    private EnemyManager enemyManager;
    private Player player;
    public Checkpoint(Playing playing, EnemyManager enemyManager, Player player) {
        this.playing = playing;
        this.enemyManager = enemyManager;
        this.player = player;
    }

    public void restore(){
        playing.setEnemyManager(this.enemyManager);
        playing.setPlayer(this.player);
    }
}
