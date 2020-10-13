package seawargame.model;

public class TransparentDeck extends SeveralShotResistantDeck {

    public TransparentDeck(Ship ship, Location shipBasedLocation) {
        super(ship, shipBasedLocation);
        this.shotsToCrash = 2;
    }
}
