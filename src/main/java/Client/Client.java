package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Main.Json;

public class Client {
    private static int serverPort;
    private static PrintWriter out;
    private static String serverCode;
    private static Socket clientSocket;
    private static String serverAddress;
    private static boolean isRunning = true;
    private static Map<String, Object> data;
    private static Map<String, Object> response;
    private static final Scanner scanner = new Scanner(System.in);
    /**
     * @param args command line arguments
     **/
    public static void runClient(String[] args) {
        try {
            setArguments(args);
            clientSocket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println(getJsonObject(serverCode));
            response = Json.reader("clientResponse.json", in.readLine());
            data = (Map<String, Object>) response.get("data");
            String message = (String) data.get("message");
            if (message.equals("You've Been Banned"))
                throw new Exception("You're Banned");
            else if (message.equals("true")) {
                System.out.println("Connected To Server " + serverAddress + "|" + serverPort + "\n");
                outThread().start();
                while (!clientSocket.isClosed() & isRunning) {
                    response = Json.reader("clientResponse.json", in.readLine());
                    data = (Map<String, Object>) response.get("data");
                    message = (String) data.get("message");
                    if (message.equals("Server ShutDown") | message.equals("Something Went Wrong") |
                        message.equals("You've Been Banned")) {
                        clientSocket.close();
                        throw new Exception(message);
                    }
                    System.out.println("\n" + message);
                    if (message.equals("Disconnecting From Server...") | message.equals("Shutting Down Server...")) {
                        System.out.print("Press Enter...");
                        clientSocket.close();
                        break;
                    }
                    else {
                        System.out.println();
                        System.out.print(" > ");
                    }
                }
            }
            else
                throw new Exception("Incorrect ServerCode: " + serverCode);
            clientSocket.close();
        }
        catch (Exception e) {
            isRunning = false;
            System.out.println();
            System.out.print("Error Connecting To Server: ");
            switch (e.getMessage()) {
                case "Connection reset", "Server ShutDown", "Cannot invoke \"String.equals(Object)\" because " +
                     "\"response\" is null", "Something Went Wrong", "You've Been Banned" -> {
                    if (e.getMessage().equals("Cannot invoke \"String.equals(Object)\" because \"response\" is null"))
                        System.out.println("Something Went Wrong");
                    else
                        System.out.println(e.getMessage());
                    System.out.print("Press Enter...");
                }
                default -> System.err.println(e.getMessage());
            }
        }
    }

    public static String getJsonObject(String message) {
        List<String> arguments = new ArrayList<>(List.of(message.toLowerCase().trim().split("\\s+")));
        String command = arguments.get(0);
        String robotName = getRobot();
        arguments.remove(0);
        return  "{\"robot\":\"" + robotName + "\"," +
                "\"command\":\"" + command + "\"," +
                "\"arguments\":" + arguments + "}";
    }

    private static void setArguments(String[] args) throws Exception {
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        String command = "connect";
        if (args.length == 1 && args[0].equals("connect")) {
            System.out.print("ServerAddress: ");
            serverAddress = scanner.nextLine().trim();
            System.out.print("ServerPort: ");
            serverPort = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("ServerCode: ");
            serverCode = scanner.nextLine().trim();
        }
        else if (args.length == 4) {
            command = args[0];
            serverAddress = args[1];
            serverPort    = Integer.parseInt(args[2]);
            serverCode    = args[3];
        }
        if (arguments.size() > 0)
            arguments.remove(0);
        if (!command.equals("connect"))
            throw new Exception("Invalid Command: '" + command + "'");
        else if (arguments.size() > 3)
            throw new Exception("Too Many Arguments: " + arguments);
    }

    private static Thread outThread() {
        return new Thread(() -> {
            System.out.print(" > ");
            while (!clientSocket.isClosed() & isRunning) {
                String message = scanner.nextLine().trim();
                out.println(getJsonObject(message));
            }
        });
    }

    private static String getRobot() {
        try {
            data = (Map<String, Object>) response.get("data");
            if (data.get("name").equals("null"))
                return "";
            return (String) data.get("name");
        }
        catch (Exception e) {
            return "";
        }
    }
}