package Command;

import Main.Main;
import Robot.Robot;
import World.Interface;

public class FailedCommand extends Command {

    public FailedCommand(String command) {
        super(command);
    }

    @Override
    public boolean execute() {
        Main.setClientResponse("Invalid Command: '" + getCommand() + "'");
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        Main.setClientResponse("Invalid Command: '" + getCommand() + "'");
        return true;
    }
}