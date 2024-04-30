package Server.Commands;

import java.util.Arrays;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class DumpCommand extends Command {

    public DumpCommand(String[] arguments) {
        super("dump", arguments);
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        if (getArguments().length == 0) {
            robot.handleCommand(Command.server("robots"), world);
            Main.setClientResponse(world.showObstacles() + "\nROBOTS:\n" + Main.getClientResponse());
        }
        else
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        return true;
    }
}