package seawargame.model.events;

import seawargame.model.IShotableUnit;

public interface IShotableUnitListener {
    void unitDamaged(IShotableUnit unit);
}
