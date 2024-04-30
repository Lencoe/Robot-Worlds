package Client.Commands;

import java.net.InetAddress;
import java.util.Map;

import Client.ClientHandler;
import Command.Command;
import Main.Main;
import Robot.Robot;
import Server.Server;
import World.Interface;

public class ChatCommand extends Command {

    public ChatCommand(String[] arguments) {
        super("chat", arguments);
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
        String message = String.join(" ", getArguments());
        if (message.split("").length < 60) {
            for (ClientHandler clientHandler : Server.getClientHandlers()) {
                if (!clientHandler.getClientAddress().equals(robot.getAddress()))
                    clientHandler.chat(robot.getName() + ": " + message);
                Main.getLog().add(robot.getName() + ": " + message);
            }
            Main.setClientResponse("Message Sent");
        }
        else {
            Main.setClientResponse("Message Too Long");
        }
        return true;
    }
}