package Client.Commands;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class RepairCommand extends Command {

    public RepairCommand(String[] arguments) {
        super("repair", arguments);
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
            if (robot.isReloading())
                robot.setStatus("Still Reloading...");
            else if (robot.isRepairing())
                robot.setStatus("Still Repairing...");
            else if (robot.getRepairCoolDown())
                robot.setStatus("Still On CoolDown...");
            else if (robot.getHealth() != robot.getMaxHealth()) {
                robot.setStatus("Repairing...");
                repair(robot);
            }
            else
                robot.setStatus("Health Already Full");
            Main.setClientResponse(robot.toString());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    private void repair(Robot robot) {
        Thread repair = new Thread(() -> {
            robot.setRepairing(true);
            try {
                Thread.sleep(10000);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.repair();
            robot.setRepairing(false);
        });
        repair.start();
        repairCoolDown(robot).start();
    }

    public Thread repairCoolDown(Robot robot) {
        return new Thread(() -> {
            robot.setRepairCoolDown(true);
            try {
                Thread.sleep(60000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.setRepairCoolDown(false);
        });
    }
}