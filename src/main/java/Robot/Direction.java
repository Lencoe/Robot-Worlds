package Robot;

import java.util.Random;

public enum Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static Direction randomDirection() {
        Random random = new Random();
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }
}