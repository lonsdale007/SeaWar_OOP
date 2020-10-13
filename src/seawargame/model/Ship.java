package seawargame.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import seawargame.model.IShotableUnit.ShotableUnitState;
import seawargame.model.events.IShipListener;
import seawargame.model.events.IShotableUnitListener;

public abstract class Ship implements Cloneable {

    public enum ShipState {
        Ok,
        Drowned
    }

    protected Set<Deck> decks;

    protected GameField field;

    protected ShipState state = ShipState.Ok;

    protected ShipObserver shipObserver;

    public Ship() {
        this.field = null;
        this.decks = new HashSet<>();
        this.state = ShipState.Ok;
        this.shipObserver = new ShipObserver();

    }

    /**
     * Существенные для расстановки направления (у 1-палубного всего 1
     * направление, у других могут быть 2 или 4 направления)
     *
     * @return
     */
    abstract public Direction[] meaningfulDirections();

    public int getDecksCount() {
        return decks.size();
    }

    public Set<Deck> getDecks() {
        return decks;
    }


    public boolean isActive() {
        for (Deck deck : decks) {
            if (deck.getState() == ShotableUnitState.Ok) {
                return true;
            }
        }
        return false;
    }

    public boolean isMyDeck(Deck deck) {
        return decks.contains(deck);
    }
    ///////!!!!!!!!!!!!!!!!!!!
    public void setup(GameField field) {
        if (this.field != null) {
            throw new RuntimeException("Cannot change field for ship !");
        } else {
            this.field = field;
        }
    }

    @Override
    public abstract Ship clone();

    /**
     * Инициализировать палубы
     */
    abstract protected void init();

    // ---------------------- Порождает события -----------------------------
    ArrayList<IShipListener> shipListeners = new ArrayList();

    public void addShipListener(IShipListener l) {
        shipListeners.add(l);
    }

    public void deleteShipLstener(IShipListener l) {
        shipListeners.remove(l);
    }

    public void fireShipDamaged() {
        for (IShipListener shipListener : shipListeners) {
            shipListener.shipDamaged();
        }
    }

    public void fireShipDrowned(IShotableUnit unit) {
        for (IShipListener shipListener : shipListeners) {
            shipListener.shipDrowned(unit);
        }
    }

    private class ShipObserver implements IShotableUnitListener {

        @Override
        public void unitDamaged(IShotableUnit unit) {
            //проверить, не затонул ли корабль
            int brokenDecks = 0;
            for (Deck obj : decks) {
                if (obj.getState() == ShotableUnitState.Broken) {
                    brokenDecks++;
                }
            }
            if (brokenDecks == getDecksCount()) {
                state = ShipState.Drowned;
                //убил
                fireShipDrowned(unit);
            } else {
                //попал
                fireShipDamaged();
            }
        }
    }

}
