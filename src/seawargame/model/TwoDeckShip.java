package seawargame.model;

public class TwoDeckShip extends StandardShip {

    public TwoDeckShip(GameField field) {
        // has 2 decks
        super(field, 2);
    }
    public TwoDeckShip() {
        super(2);
    }
}
