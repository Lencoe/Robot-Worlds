package Client.Commands;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class ForwardCommand extends Command {

    public ForwardCommand(String[] steps) {
        super("forward", steps);
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
        try {
            if (getArguments().length > 1) {
                throw new Exception();
            }
            int steps = (int) Double.parseDouble(getArguments()[0]);
            robot.setForward(true);
            if (robot.isReloading())
                robot.setStatus("Still Reloading...");
            else if (robot.isRepairing())
                robot.setStatus("Still Repairing...");
            else if (steps > robot.getMaxSteps())
                robot.setStatus("Can only move " + robot.getMaxSteps() + " steps at a time");
            else {
                Interface.UpdateResponse response = robot.updatePosition(steps);
                if (response.equals(Interface.UpdateResponse.FAILED_OBSTRUCTED))
                    robot.setStatus("There's an obstacle in the way");
                else if (response.equals(Interface.UpdateResponse.FAILED_OUTSIDE_WORLD))
                    robot.setStatus("I can't go outside my safe zone");
                else if (response.equals(Interface.UpdateResponse.SUCCESS))
                    robot.setStatus("Moved forward by " + steps + " steps");
            }
            Main.setClientResponse(robot.toString());
        }
        catch (Exception e) {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }
}