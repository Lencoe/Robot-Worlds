package World.Obstacles;

import Robot.Position;

public interface Obstacle {
    /**
     * Checks if this obstacle blocks the path that goes from coordinate (x1, y1) to (x2, y2).
     * Since our robot can only move in horizontal or vertical lines (no diagonals yet),
     * we can assume that either x1==x2 or y1==y2.
     * @param start first position
     * @param end second position
     * @return `true` if this obstacle is in the way
     **/
    default boolean blocksPath(Position start, Position end) {
        int x1 = start.getX(); 
        int y1 = start.getY();
        int x2 = end.getX();   
        int y2 = end.getY();
        if (x1 == x2) {
            int startY = Math.min(y1, y2);
            int endY = Math.max(y1, y2);
            for (int i = startY; i <= endY; i++) {
                if (blocksPosition(new Position(x1, i)))
                    return true;
            }
        }
        else if (y1 == y2) {
            int startX = Math.min(x1, x2);
            int endX = Math.max(x1, x2);
            for (int i = startX; i <= endX; i++) {
                if (blocksPosition(new Position(i, y1)))
                    return true;
            }
        }
        return false;
    }
    /**
     * Checks if this obstacle blocks access to the specified position.
     * @param position the position to check
     * @return return `true` if the x,y coordinate falls within the obstacle's area
     **/
    default boolean blocksPosition(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x >= getX() & x <= getX() + 4 & y >= getY() & y <= getY() + 4;
    }
    /**
     * Get Y coordinate of bottom left corner of obstacle.
     * @return y coordinate
     **/
    int getY();
    /**
     * Get X coordinate of bottom left corner of obstacle.
     * @return x coordinate
     **/
    int getX();
}