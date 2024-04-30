package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Client.ClientHandler;
import Main.Main;

public class Server {
    private static String serverCode;
    private static ServerSocket serverSocket;
    private static final List<InetAddress> banned = new ArrayList<>();
    private static final List<ClientHandler> clientHandlers = new ArrayList<>();
    private static final List<Map<String, Object>> clientData = new ArrayList<>();
    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    /**
     * @param serverPort port server listens on
     * @param serverCode code client uses to connect to server
     **/
    public static void runSever(int serverPort, String serverCode) {
        Server.serverCode = serverCode;
        try {
            serverSocket = new ServerSocket(serverPort);
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Server Running " + ipAddress + "|" + serverCode + "\n");
            commandThread().start();
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                if (!clientAlreadyExists(socket.getInetAddress()))
                    addNewClient(socket);
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                clientHandlers.add(clientHandler);
                thread.start();
            }
        }
        catch (IOException e) {
            commandThread().interrupt();
            if (!e.getMessage().equals("Socket closed"))
                System.err.println("Error Starting Server: " + e.getMessage());
        }
    }

    private static Thread commandThread() {
        return new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    System.out.print(" > ");
                    String input = in.readLine().trim();
                    if (serverSocket.isClosed()) {
                        break;
                    }
                    if (input.equalsIgnoreCase("quit")) {
                        for (ClientHandler clientHandler : clientHandlers) {
                            clientHandler.interrupt();
                        }
                        System.out.println();
                        System.out.println("Disconnecting All Clients...");
                        System.out.println("Shutting Down Server...");
                        shutdown();
                    }
                    else {
                        Main.runServerCommand(input);
                        System.out.println();
                        for (String i : Main.getClientResponse().split("\n")) {
                            System.out.println(i);
                        }
                        System.out.println();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void addNewClient(Socket socket) {
        Main.getLog().add("New Connection: " + socket.getInetAddress().getHostName());
        Map<String, Object> client = new HashMap<>();
        client.put("hostname", socket.getInetAddress().getHostName());
        client.put("address", socket.getInetAddress());
        client.put("connected", false);
        client.put("robot", "");
        client.put("new", true);
        clientData.add(client);
    }

    public static void connectClient(InetAddress clientAddress) {
        for (Map<String, Object> i : clientData) {
            if (i.get("address").equals(clientAddress)) {
                if (i.get("new").equals(false))
                    Main.getLog().add(clientAddress.getHostName() + " Reconnected");
                i.put("connected", true);
                i.put("new", false);
            }
        }
    }

    public static void setClientData(InetAddress clientAddress, String key, Object value) {
        for (Map<String, Object> i : clientData) {
            if (i.get("address").equals(clientAddress))
                i.put(key, value);
        }
    }

    public static void disconnectClient(InetAddress clientAddress) {
        for (Map<String, Object> i : clientData) {
            if (i.get("address").equals(clientAddress)) {
                i.put("connected", false);
                Main.getLog().add(clientAddress.getHostName() + " Disconnected");
            }
        }
    }

    private static boolean clientAlreadyExists(InetAddress clientAddress) {
        boolean clientAlreadyExists = false;
        for (Map<String, Object> i : clientData) {
            if (i.get("address").equals(clientAddress))
                clientAlreadyExists = true;
        }
        return clientAlreadyExists;
    }

    public static boolean isClientConnected(InetAddress clientAddress) {
        for (Map<String, Object> i : clientData) {
            if (i.get("address").equals(clientAddress))
                return (boolean) i.get("connected");
        }
        return true;
    }

    public static List<Map<String, Object>> getClientData() {
        return clientData;
    }

    public static List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public static List<InetAddress> getBanned() {
        return banned;
    }

    public static InetAddress getHost() {
        try {
            return InetAddress.getByName("127.0.0.1");
        }
        catch (UnknownHostException e) {
            return null;
        }
    }

    public static String getCode() {
        return serverCode;
    }

    public static void shutdown() {
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}