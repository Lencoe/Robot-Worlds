package Server.Commands;

import java.util.Arrays;
import java.util.Map;

import Client.ClientHandler;
import Command.Command;
import Main.Main;
import Robot.Robot;
import Server.Server;
import World.Interface;

public class BanCommand extends Command {

    public BanCommand(String[] arguments) {
        super("ban", arguments);
    }

    @Override
    public boolean execute() {
        ban();
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        ban();
        return true;
    }

    private void ban() {
        if (getArguments().length == 1) {
            for (ClientHandler clientHandler : Server.getClientHandlers()) {
                String address = clientHandler.getClientAddress().getHostName();
                if (address.toLowerCase().equals(getArguments()[0])) {
                    Server.getBanned().add(clientHandler.getClientAddress());
                    clientHandler.chat("You've Been Banned");
                    Server.disconnectClient(clientHandler.getClientAddress());
                    Server.getClientHandlers().remove(clientHandler);
                    for (Map.Entry<String, Robot> i : Main.getRobots().entrySet()) {
                        if (i.getValue().getAddress().getHostName().equals(address))
                            Main.getRobots().remove(i.getKey());
                    }
                    Main.setClientResponse(address + " Has Been Banned");
                    Main.getLog().add(Main.getClientResponse());
                    break;
                }
                else
                    Main.setClientResponse(address + " Doesn't Exist");
            }
        }
        else
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
    }
}