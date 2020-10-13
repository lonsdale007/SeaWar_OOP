package seawargame.model;

import java.util.HashMap;

public enum Direction {

    NORTH(0),
    SOUTH(6),
    EAST (3),
    WEST (9);

    // определяем направление в часах (0 до 12)
    private int _hours;

    Direction(int hours) {
        // Приводим заданные часы к допустимому диапазону
        this._hours = suiteHours(hours);
    }

    /** Приводим заданные часы к допустимому диапазону  */
    private static int suiteHours(int hours) {
        // Приводим заданные часы к допустимому диапазону
        hours = hours%12;
        if(hours < 0)
            hours += 12;
        return hours;
    }

    public int getClockHours() {
        return _hours;
    }
    public int getDegrees() {
        return _hours * 30;
    }

    // ------------------ Возможные направления ---------------------

    private static HashMap<Integer, Direction> directions = new HashMap <>();
    private static void addDirectionFor(Direction d) {
        int hours = d.getClockHours();
        if( ! directions.containsKey(hours) ) {
            directions.put( hours, d );
        }
    }
    private static Direction getDirectionFor(int hours) {
        hours = suiteHours(hours);
        return directions.getOrDefault(hours, NORTH);
    }
    static {
        // Add all enum constants that exist
        for(Direction d : new Direction[] {NORTH,SOUTH,EAST,WEST} ) {
            addDirectionFor(d);
        }
    }

    // Для обратной совместимости
    public static Direction north()
    { return NORTH; }

    public static Direction south()
    { return SOUTH; }

    public static Direction east()
    { return EAST; }

    public static Direction west()
    { return WEST; }


    // ------------------ Новые направления ---------------------


    public Direction clockwise() {
        return getDirectionFor(this._hours + 3);
    }

    public Direction anticlockwise() {
        return getDirectionFor(this._hours - 3);
    }

    public Direction opposite() {
        return getDirectionFor(this._hours + 6);
    }

    public Direction rightword()  {
        return clockwise();
    }

    public Direction leftword()  {
        return anticlockwise();
    }

    // ------------------ Сравнить направления ---------------------

    public boolean isOppositeTo(Direction other) {
        return this.opposite().equals(other);
    }
}
