package seawargame.model;

public class ThreeDeckShip extends StandardShip {

    public ThreeDeckShip(GameField field) {
        // has 3 decks
        super(field, 3);
    }
    public ThreeDeckShip() {
        super(3);
    }
}
