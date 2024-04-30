package Client.Commands;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class WorldCommand extends Command {

    public WorldCommand(String[] arguments) {
        super("dump", arguments);
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
            StringBuilder player = new StringBuilder("ROBOTS:");
            if (Main.getRobots().size() == 1)
                player.append("\\n0 Robots");
            for (Map.Entry<String, Robot> i : Main.getRobots().entrySet()) {
                if (!i.getValue().getName().equals(Main.getRobotName()))
                    player.append("\\n(").append(i.getValue().getName()).append(")");
            }
            String bodyCount = "KILLS: " + robot.getKillCount();
            Main.setClientResponse(player + " \\n" + bodyCount);
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }
}