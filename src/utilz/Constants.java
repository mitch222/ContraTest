package utilz;

import main.Game;

public class Constants {

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
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