package World.Obstacles;

import World.Obstacles.Obstacle;

public class SquareObstacle implements Obstacle {
    private final int x;
    private final int y;

    public SquareObstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d) to (%d,%d)", x, y, x + 4, y + 4);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}