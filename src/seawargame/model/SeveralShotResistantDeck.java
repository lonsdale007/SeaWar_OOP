package seawargame.model;

public class SeveralShotResistantDeck extends Deck {

    protected int shotsToCrash;

    public SeveralShotResistantDeck(Ship ship, Location shipBasedLocation) {
        super(ship, shipBasedLocation);
    }

    public SeveralShotResistantDeck(Ship ship, Location shipBasedLocation, int shotsToCrash) {
        super(ship, shipBasedLocation);
        this.shotsToCrash = shotsToCrash;
    }

    /**
     * Выстрелить в палубу.
     */
    @Override
    public void shootAction() {
        if (this.state != ShotableUnitState.Broken) {
            this.shotsToCrash--;
            if (this.shotsToCrash == 0) {
                this.state = ShotableUnitState.Broken;
                // в меня попали
                fireShotableUnitDamaged();
            } else {
                this.state = ShotableUnitState.Damaged;
                // в меня попали
                fireShotableUnitDamaged();
            }
        }
    }
}
