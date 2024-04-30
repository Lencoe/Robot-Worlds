package Server.Commands;

import java.util.Arrays;

import Command.Command;
import Main.Main;
import Robot.Robot;
import Server.Server;
import World.Interface;

public class LogCommand extends Command {

    public LogCommand(String[] arguments) {
        super("log", arguments);
    }

    @Override
    public boolean execute() {
        log();
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        log();
        return true;
    }

    private void log() {
        if (getArguments().length == 0) {
            StringBuilder string = new StringBuilder();
            if (Server.getClientData().isEmpty()) {
                string.append("0 Client Logs");
            }
            else {
                for (String i : Main.getLog()) {
                    string.append(i).append("\n");
                }
            }
            Main.setClientResponse(string.toString());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
    }
}