package Server.Commands;

import java.util.Arrays;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class ServerHelpCommand extends Command {

    public ServerHelpCommand(String[] arguments) {
        super("help", arguments);
    }

    @Override
    public boolean execute(Robot robot, Interface World) {
        if (getArguments().length == 0) {
            Main.setClientResponse(help());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    @Override
    public boolean execute() {
        if (getArguments().length == 0) {
            Main.setClientResponse(help());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    private String help() {
        return """
                COMMANDS:
                   log          - Display Server & World Logs
                   quit         - ShutDown Server & Disconnect All Clients
                   help         - Display Commands & Help Information
                   dump         - Display World Information
                   robots       - Display All Robots In The World
                   clients      - Display Client Information
                   obstacles    - Show World Obstacles
                   ban <user>   - Deny Client Access To Server
                   unban <user> - Allow Client Access To Server
                   kick <name>  - Remove Robot From World
                """;
    }
}