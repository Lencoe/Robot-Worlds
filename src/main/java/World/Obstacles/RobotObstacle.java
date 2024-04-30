package World.Obstacles;

import Robot.Position;

public class RobotObstacle implements Obstacle {
    private final int x;
    private final int y;

    public RobotObstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean blocksPosition(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x == getX() & y == getY();
    }

    @Override
    public String toString() {
        return  String.format("(%d,%d)", x, y);
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getX() {
        return x;
    }
}