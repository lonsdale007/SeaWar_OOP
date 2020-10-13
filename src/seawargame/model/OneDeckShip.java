package seawargame.model;

public class OneDeckShip extends StandardShip {

    public OneDeckShip(GameField field) {
        // has 1 deck
        super(field, 1);
    }
    public OneDeckShip() {
        super(1);
    }

    @Override
    public Direction[] meaningfulDirections() {
        return new Direction [] {Direction.NORTH} ;
    }
}
