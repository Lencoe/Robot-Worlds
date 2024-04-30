package Server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RunServer {
    private static int serverPort;
    private static int width = 200;
    private static int length = 200;
    private static String serverCode;
    private static int numberOfObstacles = 60;
    private static final Scanner scanner = new Scanner(System.in);
    /**
     * @param args command line arguments
     **/
    public static void runServer(String[] args) {
        try {
            setArguments(args);
            Server.runSever(serverPort, serverCode);
        }
        catch (Exception e) {
            System.out.println("Error Starting Server: " + e.getMessage());
        }
    }

    private static void setArguments(String[] args) throws Exception {
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        String command = "run";
        if (args.length == 1 && args[0].equals("run")) {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("ServerAddress: " + ipAddress);
            System.out.print("ServerPort: ");
            serverPort = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("ServerCode: ");
            serverCode = scanner.nextLine().trim();
        }
        else if (args.length == 3) {
            command = args[0];
            serverPort = Integer.parseInt(args[1]);
            serverCode = args[2];
        }
        else if (args.length == 4) {
            command = args[0];
            serverPort = Integer.parseInt(args[1]);
            serverCode = args[2];
            width = Integer.parseInt(args[3]);
        }
        else if (args.length == 5) {
            command = args[0];
            serverPort = Integer.parseInt(args[1]);
            serverCode = args[2];
            width = Integer.parseInt(args[3]);
            length = Integer.parseInt(args[4]);
        }
        else if (args.length == 6) {
            command = args[0];
            serverPort = Integer.parseInt(args[1]);
            serverCode = args[2];
            width = Integer.parseInt(args[3]);
            length = Integer.parseInt(args[4]);
            numberOfObstacles = Integer.parseInt(args[5]);
        }
        if (arguments.size() > 0)
            arguments.remove(0);
        if (!command.equals("run"))
            throw new Exception("Invalid Command: '" + command + "'");
        else if (args.length > 6)
            throw new Exception("Too Many Arguments: " + arguments);
        else if (serverCode.length() > 9)
            throw new Exception("Invalid ServerCode: Code Too Long: " + serverCode);
        else if (width < 100 | width > 200)
            throw new Exception("Invalid Width: Out Of Range: " + width);
        else if (length < 100 | length > 200)
            throw new Exception("Invalid Length: Out Of Range: " + length);
        else if (numberOfObstacles < 0 | numberOfObstacles > 60)
            throw new Exception("Invalid Value: Out Of Range " + numberOfObstacles);
    }

    public static int getNumberOfObstacles() {
        return numberOfObstacles;
    }

    public static int getLength() {
        return length;
    }

    public static int getWidth() {
        return width;
    }
}