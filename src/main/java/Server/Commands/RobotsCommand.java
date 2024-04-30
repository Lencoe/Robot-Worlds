package Server.Commands;

import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class RobotsCommand extends Command {

    public RobotsCommand(String[] arguments) {
        super("robots", arguments);
    }

    @Override
    public boolean execute() {
        robots();
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        robots();
        return true;
    }

    private void robots() {
        if (getArguments().length == 0) {
            String delimiter = "----------------------------------------------------------------\n";
            StringBuilder string = new StringBuilder();
            if (!Main.getRobots().isEmpty()) {
                string.append(delimiter).append(String.format("%-12s %-12s %-12s %-12s %6s %4s",
                        "NAME", "POSITION", "DIRECTION", "SPECIALITY", "HEALTH", "AMMO")).append("\n");
                string.append(delimiter);
                for (Map.Entry<String, Robot> i : Main.getRobots().entrySet()) {
                    string.append(i.getValue().getState()).append("\n");
                }
                string.append(delimiter);
            }
            else {
                string.append("0 Robots");
            }
            Main.setClientResponse(string.toString());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
    }
}