package Main;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Command.Command;
import Robot.Robot;
import World.Terrain.Terrain;
import World.Interface;
import World.World;

public class Main {
    private static String robotName;
    private static String clientResponse;
    private static InetAddress clientAddress;
    private static final List<String> log = new ArrayList<>();
    private static final Interface world = new World(new Terrain());
    private static final Map<String, Robot> Robots = new HashMap<>();
    private static final Map<InetAddress, String> gulag = new HashMap<>();

    public static void runClientCommand(InetAddress clientAddress, String clientMessage) {
        Command command = Command.client(clientMessage);
        robotName = getRobotName(clientAddress);
        Main.clientAddress = clientAddress;
        if (robotName.isBlank()) {
            Robot robot = new Robot("HAL");
            robot.handleCommand(command);
        }
        else {
            Robot robot = Robots.get(robotName);
            robot.handleCommand(command, world);
        }
    }

    public static void setGulag(InetAddress address, String name) {
        gulag.put(address, name);
    }

    public static String getRobotName(InetAddress clientAddress) {
        String robotName = "";
        for (Map.Entry<String, Robot> i : Robots.entrySet()) {
            if (i.getValue().getAddress().equals(clientAddress)) {
                robotName = i.getValue().getName();
            }
        }
        return robotName;
    }

    public static void runServerCommand(String clientMessage) {
        Command command = Command.server(clientMessage);
        Robot robot = new Robot("HAL");
        robot.handleCommand(command, world);
        Main.clientAddress = null;
    }

    public static void setClientResponse(String response) {
        clientResponse = response;
    }

    public static Map<InetAddress, String> getGulag() {
        return gulag;
    }

    public static Map<String, Robot> getRobots() {
        return Robots;
    }

    public static InetAddress getClientAddress() {
        return clientAddress;
    }

    public static String getClientResponse() {
        return clientResponse;
    }

    public static String getRobotName() {
        return robotName.toUpperCase();
    }

    public static List<String> getLog() {
        return log;
    }
    
    public static Interface getWorld() {
        return world;
    }
}