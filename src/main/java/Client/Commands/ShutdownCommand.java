package Client.Commands;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class ShutdownCommand extends Command {

    public ShutdownCommand(String[] arguments) {
        super("off", arguments);
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
            robot.setStatus("Shutting Down...");
            Main.setClientResponse(robot.toString());
            Main.getRobots().remove(robot.getName());
            Main.getLog().add(robot.getName() + " Left World");
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return false;
    }
}