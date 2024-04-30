package Robot;

import java.util.Map.Entry;
import java.util.Random;

import Main.Main;
import Server.RunServer;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Generates random x & y value
     * @return x & y value as new position
     **/
    public static Position randomPosition() {
        Random random = new Random();
        boolean exits = false;
        int x = random.nextInt(RunServer.getWidth());
        int y = random.nextInt(RunServer.getLength());
        if (random.nextInt(2) == 1)
            x = x * -1;
        if (random.nextInt(2) == 1)
            y = y * -1;
        while (true) {
            if (!Main.getRobots().isEmpty())
                for (Entry<String, Robot> entry : Main.getRobots().entrySet()) {
                    Robot robot = entry.getValue();
                    if (robot.getPosition().equals(new Position(x, y)))
                        exits = true;
                }
            if (exits) {
                x = random.nextInt(RunServer.getWidth());
                y = random.nextInt(RunServer.getLength());
                if (random.nextInt(2) == 1)
                    x = x * -1;
                if (random.nextInt(2) == 1)
                    y = y * -1;
            }
            else
                break;
        }
        return new Position(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Position position = (Position) o;
        if (x != position.x)
            return false;
        return y == position.y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}