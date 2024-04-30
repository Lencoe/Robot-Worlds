package World;

import java.util.List;

import Robot.Position;
import World.Terrain.Terrain;
import Server.RunServer;
import World.Obstacles.Obstacle;

public class World implements Interface {
    private static final int length = RunServer.getLength();
    private static final int width = RunServer.getWidth();
    private final List<Obstacle> obstacles;
    private final Terrain maze;

    public World(Terrain maze) {
        obstacles = maze.getObstacles();
        this.maze = maze;
    }

    @Override
    public String showObstacles() {
        StringBuilder string = new StringBuilder();
        if (!getObstacles().isEmpty()) {
            string.append("OBSTACLES:\n");
            for (Obstacle obstacle : getObstacles()) {
                string.append("- At Position ").append(obstacle.toString()).append("\n");
            }
        }
        else
            string.append("0 Obstacles\n");
        return string.toString();
    }

    @Override
    public boolean isPathBlocked(Position start, Position end) {
        return maze.blocksPath(start, end);
    }

    @Override
    public boolean isPositionAllowed(Position position) {
        int x = Math.abs(position.getX());
        int y = Math.abs(position.getY());
        return x <= width & y <= length;
    }

    @Override
    public List<Obstacle> getObstacles() {
        return obstacles;
    }
}