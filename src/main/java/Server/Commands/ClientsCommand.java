package Server.Commands;

import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import Server.Server;
import World.Interface;

public class ClientsCommand extends Command {

    public ClientsCommand(String[] arguments) {
        super("clients", arguments);
    }

    @Override
    public boolean execute() {
        clients();
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        clients();
        return true;
    }

    private void clients() {
        if (getArguments().length == 0) {
            StringBuilder string = new StringBuilder();
            if (Server.getClientData().isEmpty()) {
                string.append("0 Client Connections");
            }
            else {
                String delimiter = "--------------------------------";
                string.append(delimiter).append("\n").append(String.format("%-21s %-9s", "NAME", "CONNECTED"));
                string.append("\n").append(delimiter).append("\n");
                for (Map<String, Object> i : Server.getClientData()) {
                    string.append(String.format("%-21s %-9s", i.get("hostname"), i.get("connected"))).append("\n");
                }
                string.append(delimiter);
            }
            Main.setClientResponse(string.toString());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
    }
}