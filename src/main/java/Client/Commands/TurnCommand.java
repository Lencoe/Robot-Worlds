package Client.Commands;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class TurnCommand extends Command {

    public TurnCommand(String[] arguments) {
        super("right", arguments);
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
        if (getArguments().length == 1) {
            if (getArguments()[0].equals("right")) {
                robot.updateDirection(true);
                robot.setStatus("Turned Right");
            }
            else if (getArguments()[0].equals("left")) {
                robot.updateDirection(false);
                robot.setStatus("Turned Left");
            }
            Main.setClientResponse(robot.toString());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }
}