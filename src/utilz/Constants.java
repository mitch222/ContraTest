package utilz;

import main.Game;

public class Constants {

    public static class EnemyConstants {
        public static final int SKATER = 1;
        public static final int TURRET = 2;
        public static final int IDLE = 3;
        public static final int RUNNING = 4;
        public static final int ATTACK = 0;
        public static final int HIT = 2;
        public static final int DEAD = 1;

        public static final int SKATER_WIDTH_DEFAULT = 48;
        public static final int SKATER_HEIGHT_DEFAULT = 48;

        public static final int SKATER_WIDTH = (int) (SKATER_WIDTH_DEFAULT * Game.SCALE);
        public static final int SKATER_HEIGHT = (int) (SKATER_HEIGHT_DEFAULT * Game.SCALE);
        public static final int SKATER_DRAWOFFSET_X = (int) (7 * Game.SCALE);
        public static final int SKATER_DRAWOFFSET_Y = (int) (21 * Game.SCALE);

        public static final int TURRET_WIDTH_DEFAULT = 48;
        public static final int TURRET_HEIGHT_DEFAULT = 48;

        public static final int TURRET_WIDTH = (int) (TURRET_WIDTH_DEFAULT * Game.SCALE);
        public static final int TURRET_HEIGHT = (int) (TURRET_HEIGHT_DEFAULT * Game.SCALE);
        public static final int TURRET_DRAWOFFSET_X = (int) (7 * Game.SCALE);
        public static final int TURRET_DRAWOFFSET_Y = (int) (21 * Game.SCALE);


        public static int GetSpriteAmount(int enemy_type, int enemy_state) {

            switch (enemy_type) {
                case SKATER:
                    switch (enemy_state) {
                        case IDLE:
                            return 4;
                        case RUNNING:
                            return 6;
                        case ATTACK:
                            return 6;
                        case HIT:
                            return 2;
                        case DEAD:
                            return 6;
                    }
                case TURRET:
                    switch (enemy_state) {
                        case IDLE, RUNNING, ATTACK, DEAD:
                            return 4;
                        case HIT:
                            return 2;
                    }
            }

            return 0;

        }
        public static int GetMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case SKATER:
                    return 10;
                case TURRET:
                    return 5;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type) {
            switch (enemy_type) {
                case SKATER:
                    return 4;
                case TURRET:
                    return 1;
                default:
                    return 0;
            }

        }

    }

    public static class Projectiles{
        public static final int BULLET_DEFAULT_WIDTH = 6;
        public static final int BULLET_DEFAULT_HEIGHT = 3;
        public static final int BULLET_WIDTH = (int)(Game.SCALE * BULLET_DEFAULT_WIDTH);
        public static final int BULLET_HEIGHT = (int)(Game.SCALE * BULLET_DEFAULT_HEIGHT);
        public static final float SPEED = 1.15f * Game.SCALE;
    }
    public static class Environment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 576;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 324;
        public static final int BUILDING_FAR_WIDTH_DEFAULT = 576;
        public static final int BUILDING_FAR_HEIGHT_DEFAULT = 324;

        public static final int BUILDING_MEDIUM_WIDTH_DEFAULT = 576;
        public static final int BUILDING_MEDIUM_HEIGHT_DEFAULT = 324;
        public static final int BUILDING_CLOSE_WIDTH_DEFAULT = 576;
        public static final int BUILDING_CLOSE_HEIGHT_DEFAULT = 324;

        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
        public static final int BUILDING_FAR_WIDTH = (int) (BUILDING_FAR_WIDTH_DEFAULT * Game.SCALE);
        public static final int BUILDING_FAR_HEIGHT = (int) (BUILDING_FAR_HEIGHT_DEFAULT* Game.SCALE);
        public static final int BUILDING_MEDIUM_WIDTH = (int) (BUILDING_MEDIUM_WIDTH_DEFAULT * Game.SCALE);
        public static final int BUILDING_MEDIUM_HEIGHT = (int) (BUILDING_MEDIUM_HEIGHT_DEFAULT* Game.SCALE);
        public static final int BUILDING_CLOSE_WIDTH = (int) (BUILDING_CLOSE_WIDTH_DEFAULT * Game.SCALE);
        public static final int BUILDING_CLOSE_HEIGHT = (int) (BUILDING_CLOSE_HEIGHT_DEFAULT* Game.SCALE);
    }

    public static class UI {

        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);

        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }
    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 1;
        public static final int RUNNING = 9;
        public static final int JUMP = 3;
        public static final int GROUND = 6;

        public static final int ARMUP = 2;
        public static final int ARMUPRIGTH = 1;
        public static final int ARMRIGTH = 0;


        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case RUNNING:
                    return 6;
                case IDLE:
                    return 4;
                case JUMP:
                    return 4;
                case GROUND:
                    return 4;
                default:
                    return 1;
            }
        }
    }

}