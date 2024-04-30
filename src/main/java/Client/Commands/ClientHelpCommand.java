package Client.Commands;

import java.util.Arrays;

import Command.Command;
import Main.Main;
import Robot.Robot;
import World.Interface;

public class ClientHelpCommand extends Command {

    public ClientHelpCommand(String[] arguments) {
        super("help", arguments);
    }

    @Override
    public boolean execute(Robot robot, Interface world) {
        if (getArguments().length == 0) {
            Main.setClientResponse(help());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    @Override
    public boolean execute() {
        if (getArguments().length == 0) {
            Main.setClientResponse(help());
        }
        else {
            Main.setClientResponse("Invalid Arguments: " + Arrays.toString(getArguments()));
        }
        return true;
    }

    private String help() {
        return  "COMMANDS:\\n" +
                "   off                  - Disconnect from Server\\n" +
                "   quit                 - ShutDown Server & Disconnect All Clients\\n" +
                "   help                 - Displays Commands & Help Information\\n" +
                "   look                 - Displays Everything Robot Can See\\n" +
                "   fire                 - Fire A Shot\\n" +
                "   dump                 - Display World Info\\n" +
                "   power                - Activate Power\\n" +
                "   state                - Displays Robot Stats\\n" +
                "   repair               - Repair Health (60secs CoolDown)\\n" +
                "   reload               - Reload Weapon (30secs CoolDown)\\n" +
                "   disable              - Disable Robot & Exit World\\n" +
                "   direction            - Show Current Orientation Of Robot\\n" +
                "   back <steps>         - Move Back By Specified Number Of Steps\\n" +
                "   chat <message>       - Send A Message To Everyone In World\\n" +
                "   forward <steps>      - Move Forward By Specified Number Of Steps\\n" +
                "   turn <direction>     - Turn Right or Left by 90 Degrees\\n" +
                "   launch <name> <make> - Create & Launch Robot Into World\\n";
    }
}