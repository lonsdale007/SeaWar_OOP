package seawargame.model.events;

import seawargame.model.IShotableUnit;

public interface IShipListener{
    void shipDrowned(IShotableUnit lastHittedDeck);

    void shipDamaged();
}
