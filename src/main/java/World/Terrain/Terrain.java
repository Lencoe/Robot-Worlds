package World.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Main.Main;
import Robot.Position;
import Robot.Robot;
import Server.RunServer;
import World.Obstacles.Obstacle;
import World.Obstacles.RobotObstacle;
import World.Obstacles.SquareObstacle;

public class Terrain implements Interface {
    private final Random random = new Random();
    private final List<Obstacle> squareObstacles;
    private final static List<Obstacle> specialObstacles = new ArrayList<>();

    public Terrain() {
        squareObstacles = getSquareObstacles();
    }

    public static List<Obstacle> getRobotObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();
        if (Main.getRobots() != null)
            for (Map.Entry<String, Robot> entry : Main.getRobots().entrySet()) {
                Robot robot = entry.getValue();
                if (!robot.getName().equals(Main.getRobotName()) & !robot.isStealth()) {
                    int x = robot.getPosition().getX();
                    int y = robot.getPosition().getY();
                    if (robot.isStatue())
                        specialObstacles.add(new SquareObstacle(x, y));
                    else
                        obstacles.add(new RobotObstacle(x, y));
                }
            }
        return obstacles;
    }

    private List<Obstacle> getSquareObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();
        int numberOfObstacles = RunServer.getNumberOfObstacles();
        for (int i = 0; i < numberOfObstacles; i++) {
            int x = random.nextInt(RunServer.getWidth() - 5);
            int y = random.nextInt(RunServer.getLength() - 5);
            if (random.nextInt(2) == 1)
                x = x * -1;
            if (random.nextInt(2) == 1)
                y = y * -1;
            obstacles.add(new SquareObstacle(x, y));
        }
        obstacles.addAll(specialObstacles);
        specialObstacles.clear();
        return obstacles;
    }

    @Override
    public boolean blocksPath(Position start, Position end) {
        for (Obstacle obstacle : getObstacles()) {
            if (obstacle.blocksPath(start, end))
                return true;
        }
        return false;
    }

    @Override
    public List<Obstacle> getObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();
        obstacles.addAll(getRobotObstacles());
        obstacles.addAll(squareObstacles);
        return obstacles;
    }
}