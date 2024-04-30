package Server.Commands;

import java.util.Arrays;
import java.util.Map;

import Client.ClientHandler;
import Command.Command;
import Main.Main;
import Robot.Robot;
import Server.Server;
import World.Interface;

public class KickCommand extends Command {

    public KickCommand(String[] arguments) {
        super("kick", arguments);
    }

    @Override
    public boolean execute() {
        kick();
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        kick();
        return true;
    }

    private void kick() {
        if (getArguments().length == 1) {
            String name = getArguments()[0].toUpperCase();
            if (!Main.getRobots().isEmpty())
                for (Map.Entry<String, Robot> i : Main.getRobots().entrySet()) {
                    Robot robot = i.getValue();
                    if (robot.getName().equals(name)) {
                        for (ClientHandler clientHandler : Server.getClientHandlers()) {
                            if (clientHandler.getClientAddress().equals(robot.getAddress()))
                                clientHandler.chat("You Have Been Kicked");
                        }
                        Main.getRobots().remove(i.getKey());
                        Main.setClientResponse(name + " Has Been Kicked");
                        Main.getLog().add(Main.getClientResponse());
                    }
                    else {
                        Main.setClientResponse(name + " Doesn't Exist");
                    }
                }
            else
                Main.setClientResponse(name + " Doesn't Exist");
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
    }
}