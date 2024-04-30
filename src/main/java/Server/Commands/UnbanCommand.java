package Server.Commands;

import java.net.InetAddress;
import java.util.Arrays;

import Command.Command;
import Main.Main;
import Robot.Robot;
import Server.Server;
import World.Interface;

public class UnbanCommand extends Command {

    public UnbanCommand(String[] arguments) {
        super("ban", arguments);
    }

    @Override
    public boolean execute() {
        unban();
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        unban();
        return true;
    }

    private void unban() {
        if (getArguments().length == 1) {
            for (InetAddress i : Server.getBanned())
                if (i.getHostName().equals(getArguments()[0])) {
                    Server.getBanned().remove(i);
                    Main.setClientResponse(getArguments()[0] + " Has Been UnBanned");
                    Main.getLog().add(Main.getClientResponse());
                    break;
                }
                else
                    Main.setClientResponse(getArguments()[0] + " Doesn't Exist");
        }
        else
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
    }
}