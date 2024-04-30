package Client.Commands;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import Command.Command;
import Main.Main;
import Robot.Robot;
import Robot.Speciality;
import Server.Server;
import World.Interface;

public class LaunchCommand extends Command {

    public LaunchCommand(String[] arguments) {
        super("launch", arguments);
    }

    @Override
    public boolean execute() {
        String name;
        String speciality;
        if (getArguments().length == 2) {
            name = getArguments()[0];
            speciality = getArguments()[1];
            launchRobot(Main.getClientAddress(), name, speciality);
        }
        else if (getArguments().length == 1){
            name = getArguments()[0];
            speciality = "";
            launchRobot(Main.getClientAddress(), name, speciality);
        }
        else  {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        String name;
        String speciality;
        if (getArguments().length == 2) {
            name = getArguments()[0];
            speciality = getArguments()[1];
            launchRobot(Main.getClientAddress(), name, speciality);
        }
        else if (getArguments().length == 1){
            name = getArguments()[0];
            speciality = "";
            launchRobot(Main.getClientAddress(), name, speciality);
        }
        else  {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    private void launchRobot(InetAddress clientAddress, String name, String speciality) {
        List<String> validAbilities = new ArrayList<>();
        for (Speciality i : Speciality.values()) {
            validAbilities.add(i.toString());
        }
        if (speciality.isBlank())
            speciality = Speciality.randomSpeciality().toString();
        speciality = speciality.toUpperCase();
        name = name.toUpperCase();
        boolean invalidRobotType = !validAbilities.contains(speciality);
        boolean robotNameAlreadyExists = false;
        for (Entry<String, Robot> i : Main.getRobots().entrySet()) {
            if (i.getKey().equals(name)) {
                robotNameAlreadyExists = true;
                break;
            }
        }
        if (!Main.getRobotName(clientAddress).isBlank())
            Main.setClientResponse("Can't Launch Multiple Robots");
        else if (robotNameAlreadyExists)
            Main.setClientResponse("Name <" + name.toLowerCase() + "> Already Exits");
        else if (invalidRobotType)
            Main.setClientResponse("Type <" + speciality.toLowerCase() + "> Is Invalid");
        else if (name.length() > 9)
            Main.setClientResponse("Name <" + name.toLowerCase() + "> Too Long");
        else {
            Robot newRobot = new Robot(clientAddress, name, Speciality.valueOf(speciality), Main.getWorld());
            Main.getRobots().put(name, newRobot);
            Main.getLog().add(clientAddress.getHostName() + " Launched " + name + "|" + speciality);
            Server.setClientData(clientAddress, "robot", newRobot);
            Main.setClientResponse(newRobot.toString());
        }
    }
}