package Server.Commands;

import java.util.Arrays;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class ObstaclesCommand extends Command {

    public ObstaclesCommand(String[] arguments) {
        super("obstacles", arguments);
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        if (getArguments().length == 0)
            Main.setClientResponse(world.showObstacles());
        else
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        return true;
    }
}