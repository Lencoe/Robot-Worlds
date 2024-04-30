package World;

import java.util.List;

import Robot.Position;
import World.Obstacles.Obstacle;

public interface Interface {
    /**
     * Checks if this obstacle blocks the path that goes from coordinate (x1, y1) to (x2, y2).
     * Since our robot can only move in horizontal or vertical lines (no diagonals yet),
     * we can assume that either x1==x2 or y1==y2.
     * @param start first position
     * @param end second position
     * @return `true` if this obstacle is in the way
     **/
    boolean isPathBlocked(Position start, Position end);
    /**
     * Checks if the new position will be allowed, i.e. falls within the constraints
     * of the world, and does not overlap an obstacle.
     * @param position the position to check
     * @return true if it is allowed, else false
     **/
    boolean isPositionAllowed(Position position);
    /**
     * @return the list of obstacles, or an empty list if no obstacles exist.
     **/
    List<Obstacle> getObstacles();
    /**
     * Gives opportunity to world to draw or list obstacles.
     **/
    String showObstacles();
    /**
     * Enum that indicates response for updatePosition request
     **/
    enum UpdateResponse {
        FAILED_OUTSIDE_WORLD,
        FAILED_OBSTRUCTED,
        SUCCESS,
    }
}