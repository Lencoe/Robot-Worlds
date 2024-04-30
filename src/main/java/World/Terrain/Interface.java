package World.Terrain;

import java.util.List;

import Robot.Position;
import World.Obstacles.Obstacle;

public interface Interface {
    /**
     * Checks if this maze has at least one obstacle that blocks the path that goes from
     * coordinate (x1, y1) to (x2, y2).
     * Since our robot can only move in horizontal or vertical lines (no diagonals yet),
     * we can assume that either x1==x2 or y1==y2.
     * @param start first position
     * @param end   second position
     * @return `true` if there is an obstacle is in the way
     **/
    boolean blocksPath(Position start, Position end);
    /**
     * @return the list of obstacles, or an empty list if no obstacles exist.
     **/
    List<Obstacle> getObstacles();
}