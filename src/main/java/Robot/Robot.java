package Robot;

import java.net.InetAddress;

import Command.Command;
import Main.Main;
import Server.Server;
import World.Interface;

public class Robot {
    private final InetAddress robotAddress;
    private boolean repairCoolDown = false;
    private boolean reloadCoolDown = false;
    private boolean powerCoolDown = false;
    private final Speciality speciality;
    private boolean reloading = false;
    private boolean repairing = false;
    private boolean forward = true;
    private boolean stealth = false;
    private boolean statue = false;
    private Direction direction;
    private final Interface world;
    private final String name;
    private Position position;
    private int killCount = 0;
    private int shield = 5;
    private String status;
    private int maxHealth;
    private int maxSteps;
    private int maxRange;
    private int maxAmmo;
    private int health;
    private int range;
    private int ammo;

    public Robot(InetAddress address, String name, Speciality ability, Interface world) {
        direction = Direction.randomDirection();
        position = Position.randomPosition();
        this.name = name.toUpperCase();
        robotAddress = address;
        speciality = ability;
        this.world = world;
        status = "Ready";
        setArguments();
        health = maxHealth;
        range = maxRange;
        ammo = maxAmmo;
    }

    public Robot(String name) {
        robotAddress = null;
        speciality = null;
        this.name = name;
        world = null;
    }

    private void setArguments() {
        setMaxHealth();
        setMaxSteps();
        setMaxRange();
        setMaxAmmo();
    }

    public void updateDirection(boolean clockwise) {
        if (clockwise) {
            if (direction.equals(Direction.NORTH))
                direction = Direction.EAST;
            else if (direction.equals(Direction.EAST))
                direction = Direction.SOUTH;
            else if (direction.equals(Direction.SOUTH))
                direction = Direction.WEST;
            else if (direction.equals(Direction.WEST))
                direction = Direction.NORTH;
        }
        else {
            if (direction.equals(Direction.NORTH))
                direction = Direction.WEST;
            else if (direction.equals(Direction.EAST))
                direction = Direction.NORTH;
            else if (direction.equals(Direction.SOUTH))
                direction = Direction.EAST;
            else if (direction.equals(Direction.WEST))
                direction = Direction.SOUTH;
        }
    }

    public Interface.UpdateResponse updatePosition(int steps) {
        int newX = position.getX();
        int newY = position.getY();
        if (forward) {
            if (Direction.NORTH.equals(direction))
                newY = newY + steps;
            else if (Direction.EAST.equals(direction))
                newX = newX + steps;
            else if (Direction.SOUTH.equals(direction))
                newY = newY - steps;
            else if (Direction.WEST.equals(direction))
                newX = newX - steps;
        }
        else
            if (Direction.NORTH.equals(direction))
                newY = newY - steps;
            else if (Direction.EAST.equals(direction))
                newX = newX - steps;
            else if (Direction.SOUTH.equals(direction))
                newY = newY + steps;
            else if (Direction.WEST.equals(direction))
                newX = newX + steps;

        Position newPosition = new Position(newX, newY);
        if (!world.isPathBlocked(position, newPosition)) {
            if (world.isPositionAllowed(newPosition)) {
                position = newPosition;
                return Interface.UpdateResponse.SUCCESS;
            }
            return Interface.UpdateResponse.FAILED_OUTSIDE_WORLD;
        }
        return Interface.UpdateResponse.FAILED_OBSTRUCTED;
    }

    public String getState() {
        return  String.format("%-12s %-12s %-12s %-12s %6s %4s", name, position, direction, speciality, health, ammo);
    }

    @Override
    public String toString() {
        return "[" + position.getX() + "," + position.getY() + "] " + name + ": " + status;
    }

    public void handleCommand(Command command, Interface world) {
        command.execute(this, world);
    }

    public void removeRobot() {
        Main.getRobots().remove(name);
        Main.setGulag(robotAddress, name);
        Server.setClientData(robotAddress, "robot", "");
    }

    public void takeDamage() {
        if (speciality.equals(Speciality.SHIELD) & shield != 0)
            shield --;
        else if (health != 0)
            health--;
        else
            removeRobot();
    }

    private void setMaxHealth() {
        if (speciality.equals(Speciality.STRENGTH))
            maxHealth = 15;
        else
            maxHealth = 10;
    }

    private void setMaxAmmo() {
        if (speciality.equals(Speciality.SKIT))
            maxAmmo = 10;
        else
            maxAmmo = 5;
    }

    private void setMaxSteps() {
        if (speciality.equals(Speciality.SPEED))
            maxSteps = 20;
        else
            maxSteps = 10;
    }

    private void setMaxRange() {
        if (speciality.equals(Speciality.SNIPER))
            maxRange = 40;
        else
            maxRange = 20;
    }

    public void setRepairCoolDown(boolean bool) {
        repairCoolDown = bool;
    }

    public void setReloadCoolDown(boolean bool) {
        reloadCoolDown = bool;
    }

    public void setPowerCoolDown(boolean bool) {
        powerCoolDown = bool;
    }

    public void handleCommand(Command command) {
        command.execute();
    }

    public void setRepairing(boolean bool) {
        repairing = bool;
    }

    public void setReloading(boolean bool) {
        reloading = bool;
    }

    public void setStealth(boolean bool) {
        stealth = bool;
    }

    public void setStatue(boolean bool) {
        statue = bool;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setForward(boolean bool) {
        forward = bool;
    }

    public boolean getRepairCoolDown() {
        return repairCoolDown;
    }

    public boolean getReloadCoolDown() {
        return reloadCoolDown;
    }

    public boolean getPowerCoolDown() {
        return powerCoolDown;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public Direction getDirection() {
        return direction;
    }

    public InetAddress getAddress() {
        return robotAddress;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isReloading() {
        return reloading;
    }

    public boolean isRepairing() {
        return repairing;
    }

    public void setKillCount() {
        killCount++;
    }

    public boolean isStealth() {
        return stealth;
    }

    public String getStatus() {
        return status;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isStatue() {
        return statue;
    }

    public int getKillCount() {
        return killCount;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public Interface getWorld() {
        return world;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public String getName() {
        return name;
    }

    public void fireShot() {
        ammo--;
    }

    public int getHealth() {
        return health;
    }

    public int getRange() {
        return range;
    }

    public int getAmmo() {
        return ammo;
    }

    public void reload() {
        ammo = maxAmmo;
    }

    public void repair() {
        health = maxHealth;
    }
}