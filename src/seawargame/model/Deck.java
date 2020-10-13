package seawargame.model;

import java.util.ArrayList;
import seawargame.model.events.IShotableUnitListener;

public abstract class Deck implements IShotableUnit{

    protected Cell cell;

    protected Location shipBasedLocation;

    protected ShotableUnitState state;

    public Deck(Ship ship, Location shipBasedLocation){

        cell = null;
        this.shipBasedLocation = shipBasedLocation;
        this.state = ShotableUnitState.Ok;

    }

    /**
     * @return the cell
     */
    public Cell getCell() {
        return cell;
    }

    @Override
    public ShotableUnitState getState() {
        return this.state;
    }

    public Location getShipBasedLocation(){
        return shipBasedLocation;
    }

    /**
     * @param cell the cell to set
     */
    public void setCell(Cell cell) {
        if(cell != null) {
            this.cell = cell;
            cell.setDeck(this);
        }
    }
    public void unsetCell() {
        if(this.cell != null) {
            Cell _cell = this.cell;
            this.cell = null;
            _cell.unsetDeck();
        }
    }

    /** Ударить в палубу.
     * Для изменения стандартного поведения переопределить метод в подклассах Deck
     *   */
    @Override
    public void shootAction() {
        if (this.state != ShotableUnitState.Broken){
            this.state = ShotableUnitState.Broken;
            // в меня попали
            fireShotableUnitDamaged();
        }

    }

    ArrayList<IShotableUnitListener> modelListeners = new ArrayList<>();
    ArrayList<IShotableUnitListener> buttonListeners = new ArrayList<>();

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
        for (IShotableUnitListener listener : modelListeners){
            listener.unitDamaged(this);
        }
    }
}
