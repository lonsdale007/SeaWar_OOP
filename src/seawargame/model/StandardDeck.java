package seawargame.model;

public class StandardDeck extends SeveralShotResistantDeck{

    public StandardDeck(Ship ship, Location shipBasedLocation) {
        super(ship, shipBasedLocation);
        this.shotsToCrash = 1;

    }
}
