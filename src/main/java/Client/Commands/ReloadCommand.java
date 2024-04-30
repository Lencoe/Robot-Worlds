package Client.Commands;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class ReloadCommand extends Command {

    public ReloadCommand(String[] arguments) {
        super("reload", arguments);
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
            else if (robot.getReloadCoolDown())
                robot.setStatus("Still On CoolDown...");
            else if (robot.getAmmo() != robot.getMaxAmmo()) {
                robot.setStatus("Reloading...");
                reload(robot);
            }
            else
                robot.setStatus("Ammo Already Full");
            Main.setClientResponse(robot.toString());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    private void reload(Robot robot) {
        Thread reload = new Thread(() -> {
            robot.setReloading(true);
            try {
                Thread.sleep(10000);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.reload();
            robot.setReloading(false);
        });
        reload.start();
        reloadCoolDown(robot).start();
    }

    public Thread reloadCoolDown(Robot robot) {
        return new Thread(() -> {
            robot.setReloadCoolDown(true);
            try {
                Thread.sleep(60000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.setReloadCoolDown(false);
        });
    }
}