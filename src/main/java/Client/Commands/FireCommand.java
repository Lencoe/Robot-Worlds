package Client.Commands;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Client.ClientHandler;
import Command.Command;
import Robot.Direction;
import Main.Main;
import Robot.Position;
import Robot.Robot;
import World.Terrain.Terrain;
import Server.Server;
import World.Interface;
import World.Obstacles.Obstacle;

public class FireCommand extends Command {

    public FireCommand(String[] arguments) {
        super("fire", arguments);
    }

    @Override
    public boolean execute() {
        StringBuilder response = new StringBuilder("You Don't Have Robot In World");
        for (Map.Entry<InetAddress, String> i : Main.getGulag().entrySet()) {
            if (Main.getClientAddress().equals(i.getKey()))
                response.append(" \\n(").append(i.getValue()).append(" Died)");
        }
        Main.setClientResponse(response.toString());
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        if (getArguments().length == 0) {
            if (robot.isReloading())
                robot.setStatus("Still Reloading...");
            else if (robot.isRepairing())
                robot.setStatus("Still Repairing...");
            else if (robot.getAmmo() != 0) {
                robot.fireShot();
                List<Robot> lookRobot = lookRobot(robot);
                List<Obstacle> lookObs = lookObstacles(robot, world);
                if (!lookRobot.isEmpty()) {
                    Robot robot2 = lookRobot.get(0);
                    if (robot2.getHealth() == 0) {
                        robot.setKillCount();
                        robot.setStatus("You Obliterated " + robot2.getName());
                        Main.getLog().add(robot.getName() + " Obliterated " + robot2.getName());
                    }
                    else {
                        robot.setStatus("Hit " + robot2.getName());
                        for (ClientHandler clientHandler : Server.getClientHandlers()) {
                            if (clientHandler.getClientAddress().equals(robot2.getAddress()))
                                clientHandler.chat(robot.getName() + " Shot You");
                        }
                    }
                    lookRobot.get(0).takeDamage();
                }
                else if (!lookObs.isEmpty())
                    robot.setStatus("Hit Obstacle");
                else
                    robot.setStatus("Hit Nothing");
            }
            else
                robot.setStatus("Out Of Ammo");
            Main.setClientResponse(robot.toString());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    public static List<Robot> lookRobot(Robot robot) {
        int range = robot.getRange();
        int x = robot.getPosition().getX();
        int y = robot.getPosition().getY();
        List<Position> temp = new ArrayList<>();
        List<Position> north = new ArrayList<>();
        for (int i = 0; i <= range; i++) {
            if (robot.getDirection().equals(Direction.NORTH))
                temp.add(new Position(x, y + i));
            else if (robot.getDirection().equals(Direction.SOUTH))
                temp.add(new Position(x, y - i));
            else if (robot.getDirection().equals(Direction.WEST))
                temp.add(new Position(x - i, y));
            else if (robot.getDirection().equals(Direction.EAST))
                temp.add(new Position(x + i, y));
        }
        for (Obstacle obstacle : Terrain.getRobotObstacles()) {
            for (int i = 0; i <= range; i++) {
                if (obstacle.blocksPosition(temp.get(i)))
                    north.add(new Position(obstacle.getX(), obstacle.getY()));
            }
        }
        if (!north.isEmpty())
            for (Map.Entry<String, Robot> entry : Main.getRobots().entrySet()) {
                Robot robot2 = entry.getValue();
                if (!robot2.getName().equals(Main.getRobotName())) {
                    int x2 = robot2.getPosition().getX();
                    int y2 = robot2.getPosition().getY();
                    if (north.get(0).getX() == x2 & north.get(0).getY() == y2)
                        return new ArrayList<>(List.of(robot2));
                }
            }
        return new ArrayList<>();
    }

    public static List<Obstacle> lookObstacles(Robot robot, Interface world) {
        int range = robot.getRange();
        int x = robot.getPosition().getX();
        int y = robot.getPosition().getY();
        List<Position> temp = new ArrayList<>();
        List<Obstacle> north = new ArrayList<>();
        for (int i = 0; i <= range; i++) {
            if (robot.getDirection().equals(Direction.NORTH))
                temp.add(new Position(x, y + i));
            else if (robot.getDirection().equals(Direction.SOUTH))
                temp.add(new Position(x, y - i));
            else if (robot.getDirection().equals(Direction.WEST))
                temp.add(new Position(x - i, y));
            else if (robot.getDirection().equals(Direction.EAST))
                temp.add(new Position(x + i, y));
        }
        for (Obstacle obstacle : world.getObstacles()) {
            for (int i = 0; i <= range; i++) {
                if (obstacle.blocksPosition(temp.get(i)))
                    north.add(obstacle);
            }
        }
        return north;
    }
}