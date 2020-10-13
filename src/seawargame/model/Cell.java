package seawargame.model;

import java.util.ArrayList;
import seawargame.model.events.IShotableUnitListener;

public class Cell implements IShotableUnit {

    private Location location;
    private Deck deck;
    private ShotableUnitState state;

    public Cell(Location location) {;

        this.location = location;
        this.state = ShotableUnitState.Ok;
    }

    @Override
    public void shootAction() {
        if (this.deck == null && this.state != ShotableUnitState.Broken) {
            this.state = ShotableUnitState.Broken;
            // в меня попали
            fireShotableUnitDamaged();
        }
    }

    void openAction() {
        if (this.deck == null) {
            this.state = ShotableUnitState.Broken;
            // меня открыли
            fireCellOpened();
        }
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public ShotableUnitState getState() {
        return this.state;
    }

    /**
     * @return the deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * @param deck the deck to set
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
        if (this.deck != null && this.deck.getCell() != this) {
            this.deck.setCell(this);
        }
    }

    public void unsetDeck() {
        if (this.deck != null && this.deck.getCell() != null) {
            this.deck.unsetCell();
        }
        this.deck = null;
    }

    ArrayList<IShotableUnitListener> modelListeners = new ArrayList<IShotableUnitListener>();

    ArrayList<IShotableUnitListener> buttonListeners = new ArrayList<IShotableUnitListener>();

    @Override
    public void addModelUnitListener(IShotableUnitListener l) {
        modelListeners.add(l);
    }

    @Override
    public void addButtonUnitListener(IShotableUnitListener l) {
        buttonListeners.add(l);
    }

    @Override
    public void deleteModelUnitListener(IShotableUnitListener l) {
        buttonListeners.remove(l);
    }

    @Override
    public void deleteButtonUnitListener(IShotableUnitListener l) {
        modelListeners.remove(l);
    }

    @Override
    public void fireShotableUnitDamaged() {
        for (IShotableUnitListener listener : buttonListeners) {
            listener.unitDamaged(this);
        }
        for (IShotableUnitListener listener : modelListeners) {
            listener.unitDamaged(this);
        }

    }

    public void fireCellOpened() {
        for (IShotableUnitListener listener : buttonListeners) {
            listener.unitDamaged(this);
        }
    }
}
