package seawargame.model;

public class Location {

    private int x,y;

    /**
     * @param x col (column)
     * @param y row
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // пары синонимов
    public int x() {
        return x;
    }
    public int col() {
        return x;
    }

    public int y() {
        return y;
    }
    public int row() {
        return y;
    }

    // ============================ Преобразования ============================

    /** Сдвинутая позиция
     * @return new Location
     */
    public Location movedBy(int shift_x, int shift_y) {
        return new Location(x() + shift_x, y() + shift_y);
    }
    /** Позиция, повёрнутая относительно базовой позиции в заданном направлении. Базовым направлением считается Direction.north().
     * @return new Location
     */
    public Location rotatedBy(Location basePoint, Direction direction) {
        int diff_x = x() - basePoint.x();
        int diff_y = y() - basePoint.y();

        switch(direction) {
            case NORTH:
                return this; //.clone();

            case  EAST:
                return new Location(basePoint.x() - diff_y, basePoint.y() + diff_x);

            case SOUTH:
                return new Location(basePoint.x() - diff_x, basePoint.y() - diff_y);

            case WEST:
                return new Location(basePoint.x() + diff_y, basePoint.y() - diff_x);

            default:
                return null;
        }
    }

    // ============== Общие методы класса Object =================

    @Override
    public Location clone() throws CloneNotSupportedException{
        Location copy = (Location)super.clone();
        return copy;
    }

    @Override
    public String toString() {
        return ((char)('A'+x())) +""+ (y()+1);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Location) {
            return ((Location)o).x() == x()
                    && ((Location)o).y() == y();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 1024 * this.x + this.y;
    }

}
