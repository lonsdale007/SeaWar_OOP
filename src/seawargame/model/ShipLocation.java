package seawargame.model;

public class ShipLocation implements Cloneable {
    private Location location;
    private Direction direction;

    public ShipLocation(Location location, Direction direction) {
        this.location = location;
        this.direction = direction;
    }

    /**
     * @return the location
     */
    public Location location() {
        return location;
    }

    /**
     * @return the direction
     */
    public Direction direction() {
        return direction;
    }
}
