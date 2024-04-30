package Client.Commands;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Position;
import Robot.Robot;
import World.Terrain.Terrain;
import Server.RunServer;
import World.Interface;
import World.Obstacles.Obstacle;

public class LookCommand extends Command {

    public LookCommand(String[] arguments) {
        super("look", arguments);
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
            int x = robot.getPosition().getX();
            int y = robot.getPosition().getY();
            List<Position> north = new ArrayList<>();
            List<Position> south = new ArrayList<>();
            List<Position> west = new ArrayList<>();
            List<Position> east = new ArrayList<>();
            List<Position> northeast = new ArrayList<>();
            List<Position> northwest = new ArrayList<>();
            List<Position> southeast = new ArrayList<>();
            List<Position> southwest = new ArrayList<>();
            StringBuilder stringRobotObstacles = new StringBuilder();
            stringRobotObstacles.append("ROBOTS:\\n");
            StringBuilder stringSquareObstacles = new StringBuilder();
            stringSquareObstacles.append("OBSTACLES:\\n");
            int range = robot.getRange();
            for (int i = x-range; i <= x+range; i++) {
                for (int j = y-range; j <= y+range; j++) {
                    if (i <= 5 && i >= -5 && j < 0)
                        south.add(new Position(i, j));
                    else if (i <= 5 && i >= -5 && j > 0)
                        north.add(new Position(i, j));
                    else if (j <= 5 && j >= -5 && i > 0)
                        east.add(new Position(i, j));
                    else if (j <= 5 && j >= -5 && i < 0)
                        west.add(new Position(i, j));
                    else if (i > 0 && j > 0)
                        northeast.add(new Position(i, j));
                    else if (i > 0)
                        southeast.add(new Position(i, j));
                    else if (i < 0 && j > 0)
                        northwest.add(new Position(i, j));
                    else if (i < 0)
                        southwest.add(new Position(i, j));
                }
            }
            List<Position> allDirections = new ArrayList<>();
            allDirections.addAll(north);
            allDirections.addAll(south);
            allDirections.addAll(west);
            allDirections.addAll(east);
            allDirections.addAll(northwest);
            allDirections.addAll(northeast);
            allDirections.addAll(southwest);
            allDirections.addAll(southeast);
            List<String> tempList = new ArrayList<>();
            for (Obstacle obstacle : world.getObstacles()) {
                for (Position position : allDirections) {
                    if (obstacle.blocksPosition(position)) {
                        String tempString = "";
                        String obstacleStr = "At Position " + obstacle;
                        if (north.contains(position))
                            tempString = " NORTH\\n";
                        else if (south.contains(position))
                            tempString = " SOUTH\\n";
                        else if (west.contains(position))
                            tempString = " WEST\\n";
                        else if (east.contains(position))
                            tempString = " EAST\\n";
                        else if (northwest.contains(position))
                            tempString = " NORTH-WEST\\n";
                        else if (southwest.contains(position))
                            tempString = " SOUTH-WEST\\n";
                        else if (northeast.contains(position))
                            tempString = " NORTH-EAST\\n";
                        else if (southeast.contains(position))
                            tempString = " SOUTH-EAST\\n";
                        else if (position.getY() < (-RunServer.getLength()))
                            tempString = " SOUTH-EDGE\\n";
                        else if (position.getY() > RunServer.getLength())
                            tempString = " NORTH-EDGE\\n";
                        else if (position.getX() < (-RunServer.getWidth()))
                            tempString = " WEST-EDGE\\n";
                        else if (position.getX() > RunServer.getWidth())
                            tempString = " EAST-EDGE\\n";
                        if (!tempList.contains(obstacleStr)) {
                            tempList.add(obstacleStr);
                            stringSquareObstacles.append(obstacleStr).append(tempString);
                        }
                    }
                }
            }
            for (Obstacle obstacle : Terrain.getRobotObstacles()) {
                for (Position position : allDirections) {
                    if (obstacle.blocksPosition(position)) {
                        String obstacleStr = "At Position " + obstacle;
                        if (north.contains(position))
                            stringRobotObstacles.append(obstacleStr).append(" NORTH\\n");
                        else if (south.contains(position))
                            stringRobotObstacles.append(obstacleStr).append(" SOUTH\\n");
                        else if (west.contains(position))
                            stringRobotObstacles.append(obstacleStr).append(" WEST\\n");
                        else if (east.contains(position))
                            stringRobotObstacles.append(obstacleStr).append(" EAST\\n");
                        else if (northwest.contains(position))
                            stringRobotObstacles.append(obstacleStr).append(" NORTH-WEST\\n");
                        else if (southwest.contains(position))
                            stringRobotObstacles.append(obstacleStr).append(" SOUTH-WEST\\n");
                        else if (northeast.contains(position))
                            stringRobotObstacles.append(obstacleStr).append(" NORTH-EAST\\n");
                        else if (southeast.contains(position))
                            stringRobotObstacles.append(obstacleStr).append(" SOUTH-EAST\\n");
                    }
                }
            }
            if (stringRobotObstacles.toString().split("\\\\n").length == 1)
                stringRobotObstacles.append("0 Robots\\n");
            if (stringSquareObstacles.toString().split("\\\\n").length == 1) {
                stringSquareObstacles.append("0 Obstacles");
                Main.setClientResponse(stringRobotObstacles.toString() + stringSquareObstacles);
            }
            else  
                Main.setClientResponse(stringRobotObstacles + stringSquareObstacles.substring(0, stringSquareObstacles.length() - 2));
        }
        else 
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        return true;
    }
}