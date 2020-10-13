package seawargame.model;

import seawargame.model.events.IShotableUnitListener;

public interface IShotableUnit {

    enum ShotableUnitState {
        Ok, Damaged, Broken
    }

    ShotableUnitState getState();

    void shootAction();

    void addModelUnitListener(IShotableUnitListener l);

    void addButtonUnitListener(IShotableUnitListener l);

    void fireShotableUnitDamaged();
}
