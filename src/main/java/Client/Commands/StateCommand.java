package Client.Commands;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class StateCommand extends Command {

    public StateCommand(String[] arguments) {
        super("state", arguments);
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
            String delimiter = "----------------------------------------------------------------";
            Main.setClientResponse(delimiter + "\\n" + String.format("%-12s %-12s %-12s %-12s %6s %4s",
                    "ROBOTNAME", "POSITION", "DIRECTION", "SPECIALITY", "HEALTH", "AMMO") +
                    "\\n" + delimiter + "\\n" + robot.getState() + "\\n" + delimiter);
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }
}