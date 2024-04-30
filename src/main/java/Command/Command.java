package Command;

import java.util.ArrayList;
import java.util.List;

import Client.Commands.*;
import Robot.Robot;
import Server.Commands.*;
import World.Interface;

public abstract class Command {
    private final String command;
    private final String[] arguments;

    public static Command client(String clientMessage) {
        List<String> args = new ArrayList<>(List.of(clientMessage.toLowerCase().trim().split("\\s+")));
        String command = args.get(0);
        args.remove(0);
        String[] arguments = args.toArray(new String[0]);
        return switch (command) {
            case "help"      -> new ClientHelpCommand(arguments);
            case "direction" -> new DirectionCommand(arguments);
            case "disable"   -> new ShutdownCommand(arguments);
            case "back"      -> new BackwardCommand(arguments);
            case "forward"   -> new ForwardCommand(arguments);
            case "reload"    -> new ReloadCommand(arguments);
            case "launch"    -> new LaunchCommand(arguments);
            case "repair"    -> new RepairCommand(arguments);
            case "state"     -> new StateCommand(arguments);
            case "power"     -> new PowerCommand(arguments);
            case "dump"      -> new WorldCommand(arguments);
            case "fire"      -> new FireCommand(arguments);
            case "look"      -> new LookCommand(arguments);
            case "turn"      -> new TurnCommand(arguments);
            case "chat"      -> new ChatCommand(arguments);
            default          -> new FailedCommand(command);
        };
    }

    public static Command server(String clientMessage) {
        List<String> args = new ArrayList<>(List.of(clientMessage.toLowerCase().trim().split("\\s+")));
        String command = args.get(0);
        args.remove(0);
        String[] arguments = args.toArray(new String[0]);
        return switch (command) {
            case "help"      -> new ServerHelpCommand(arguments);
            case "obstacles" -> new ObstaclesCommand(arguments);
            case "clients"   -> new ClientsCommand(arguments);
            case "robots"    -> new RobotsCommand(arguments);
            case "unban"     -> new UnbanCommand(arguments);
            case "dump"      -> new DumpCommand(arguments);
            case "kick"      -> new KickCommand(arguments);
            case "log"       -> new LogCommand(arguments);
            case "ban"       -> new BanCommand(arguments);
            default          -> new FailedCommand(command);
        };
    }

    public abstract boolean execute(Robot robot, Interface world);

    public Command(String command, String[] arguments) {
        this.arguments = arguments;
        this.command = command;
    }

    public abstract boolean execute();

    public Command(String command) {
        this.arguments = new String[0];
        this.command = command;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getCommand() {
        return command;
    }
}