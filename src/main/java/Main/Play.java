package Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Play {
    public static void main(String[] args) {
        List<String> templist = new ArrayList<>(List.of(args));
        String command = "";
        if (args.length != 0) {
            command = templist.get(0);
            templist.remove(0);
        }
        String[] arguments = templist.toArray(new String[0]);
        if (command.equals("help") & arguments.length == 0 || args.length == 0)
            System.out.print(help());
        else if (command.equals("help") & arguments.length > 1)
            System.out.println("Invalid Arguments: " + Arrays.toString(arguments));
        else if (command.equals("connect"))
            Client.Client.runClient(args);
        else if (command.equals("run"))
            Server.RunServer.runServer(args);
        else
            System.out.println("Invalid Command: '" + command + "'");
    }

    private static String help() {
        return """
                COMMANDS:
                    help                               - Display Commands & Help Information
                    connect <address> <port> <code>    - Connect To Server On Specified Address & Port
                    run <port> <code> <width> <length> - Run Server On Specified Port
               """;
    }
}