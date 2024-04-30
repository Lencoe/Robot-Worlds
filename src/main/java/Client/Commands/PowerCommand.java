package Client.Commands;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import Robot.Speciality;
import World.Interface;

public class PowerCommand extends Command {

    public PowerCommand(String[] arguments) {
        super("power", arguments);
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
        if (getArguments().length == 0) {
            if (!robot.getPowerCoolDown()) {
                if (robot.getSpeciality().equals(Speciality.STATUE) |
                    robot.getSpeciality().equals(Speciality.STEALTH)) {
                    activatePower(robot);
                    Main.setClientResponse("Speciality Activated");
                }
                else {
                    Main.setClientResponse("Invalid Command: '" + getCommand() + "'");
                }
            }
            else {
                Main.setClientResponse("Still On CoolDown...");
            }
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    private void activatePower(Robot robot) {
        Thread statue = new Thread(() -> {
            robot.setStatue(true);
            try {
                Thread.sleep(60000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.setStatue(false);
        });
        Thread stealth = new Thread(() -> {
            robot.setStealth(true);
            try {
                Thread.sleep(60000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.setStealth(false);
        });
        if (robot.getSpeciality().equals(Speciality.STATUE))
            statue.start();
        else if (robot.getSpeciality().equals(Speciality.STEALTH))
            stealth.start();
        powerCoolDown(robot).start();
    }

    private Thread powerCoolDown(Robot robot) {
        return new Thread(() -> {
            robot.setPowerCoolDown(true);
            try {
                Thread.sleep(60000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.setPowerCoolDown(false);
        });
    }
}