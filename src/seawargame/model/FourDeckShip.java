package seawargame.model;

public class FourDeckShip extends StandardShip {

    public FourDeckShip(GameField field) {
        // has 4 decks
        super(field, 4);
    }
    public FourDeckShip() {
        super(4);
    }
}
