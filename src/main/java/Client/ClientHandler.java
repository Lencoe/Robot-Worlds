package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Main.Json;
import Main.Main;
import Robot.Robot;
import Server.Server;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private InetAddress clientAddress;
    private boolean isRunning = true;
    private PrintStream out;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintStream(clientSocket.getOutputStream());
            clientAddress = clientSocket.getInetAddress();
            while (isRunning) {
                String clientMessage = in.readLine();
                if (clientMessage == null)
                    clientMessage = "{\"robot\":\"\",\"command\":\"\",\"arguments\":[]}";
                Map<String, Object> request = Json.reader("clientRequest.json", clientMessage);
                List<String> arguments = new ArrayList<>();
                List<Object> args = (List<Object>) request.get("arguments");
                for (Object i : args) {
                    arguments.add(String.valueOf(i));
                }
                String command = request.get("command") + " " + String.join(" ", arguments);
                if (Server.getBanned().contains(clientAddress))
                    out.println(getJsonObject("You've Been Banned"));
                else if (request.get("command").equals(Server.getCode())) {
                    if (!Server.isClientConnected(clientAddress))
                        Server.connectClient(clientAddress);
                    out.println(getJsonObject("true"));
                }
                else if (!ClientMessageHandler(command.trim()))
                    break; 
            }
            clientSocket.close();
        }
        catch (Exception e) {
            if (!Server.getBanned().contains(clientAddress))
                Server.disconnectClient(clientAddress);
            if (!e.getMessage().equals("Connection reset") & !e.getMessage()
                .equals("Cannot invoke \"String.equals(Object)\" because \"clientMessage\" is null")) {
                Main.getLog().add("Error Handling " + clientAddress.getHostName() + ": " + e.getMessage());
                Main.getLog().add(Arrays.toString(e.getStackTrace()));
                out.println(getJsonObject("Something Went Wrong"));
            }
        }
    }

    private boolean ClientMessageHandler(String command) {
        if (command.equalsIgnoreCase("off")) {
            out.println(getJsonObject("Disconnecting From Server..."));
            Server.disconnectClient(clientAddress);
            return false;
        }
        else if (command.equalsIgnoreCase("quit")) {
            if (!clientAddress.equals(Server.getHost())) {
                out.println(getJsonObject("You're Not Server Host"));
                return true;
            }
            else {
                System.out.println();
                System.out.println();
                System.out.println("Disconnecting All Clients...");
                System.out.println("Shutting Down Server...");
                out.println(getJsonObject("Shutting Down Server..."));
                System.out.print("Press Enter...");
                isRunning = false;
                Server.shutdown();
                return false;
            }
        }
        Main.runClientCommand(clientAddress, command);
        Server.setClientData(clientAddress, "response", Main.getClientResponse());
        Server.setClientData(clientAddress, "request", command);
        out.println(getJsonObject(Main.getClientResponse()));
        Json.writer("clientData.json", Server.getClientData());
        return true;
    }

    private String getJsonObject(String message) {
        boolean result = true;
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> state = new HashMap<>();
        if (Main.getRobots().get(Main.getRobotName(clientAddress)) != null) {
            Robot robot = Main.getRobots().get(Main.getRobotName(clientAddress));
            data.put("shots", "\"" + robot.getMaxAmmo() + "\"");
            data.put("range", "\"" + robot.getRange() + "\"");
            data.put("name", "\"" + robot.getName() + "\"");
            data.put("steps", "\"" + robot.getMaxSteps() + "\"");
            data.put("hits", "\"" + robot.getMaxHealth() + "\"");
            data.put("repair", "\"10seconds\"");
            data.put("reload", "\"5seconds\"");
            data.put("speciality", "\"" + robot.getSpeciality() + "\"");
            state.put("ammo", "\"" + robot.getAmmo() + "\"");
            state.put("status", "\"" + robot.getStatus() + "\"");
            state.put("health", "\"" + robot.getHealth() + "\"");
            state.put("position", "\"" + robot.getPosition() + "\"");
            state.put("direction", "\"" + robot.getDirection() + "\"");
        }
        data.put("message", "\"" + message + "\"");
        return "{\"result\":" + result + "," +
                "\"data\":" + data + "," +
                "\"state\":" + state + "}";
    }

    public void interrupt() {                                                                                                                                                                                                                                           
        out.println(getJsonObject("Server ShutDown"));
        isRunning = false;
        Server.shutdown();
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public void chat(String message) {
        out.println(getJsonObject("\\n" + message));
    }
}