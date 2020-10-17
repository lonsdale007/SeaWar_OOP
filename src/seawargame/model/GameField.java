package seawargame.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import seawargame.util.Utils;
import seawargame.model.events.IGameFieldListener;
import seawargame.model.events.IShipListener;
import seawargame.model.events.IShotableUnitListener;

public class GameField {

    private List<Ship> ships = new ArrayList<>();
    private int size;
    private Map<Location, Cell> cells;
    private ShotableUnitObserver shotableUnitObserver;
    private ShipObserver shipObserver;
    private Cell lastHitCell;

    public GameField() {
        this.cells = new HashMap<>();
        this.shotableUnitObserver = new ShotableUnitObserver();
        this.size = 0;
        this.lastHitCell = null;
        this.shipObserver = new ShipObserver();
    }

    public GameField(int fieldSize) {
        this();
        init(fieldSize);
    }

    public boolean hasActiveShips() {
        for (Ship ship : ships) {
            if (ship.isActive()) {
                return false;
            }
        }

        return true;
    }


    private Cell[] emptyCells() {
        // найти незанятые ячейки...
        // все
        Set<Cell> emptyCells = new HashSet<>(cells.values());
        // вычесть занятые
        emptyCells.removeIf((c) -> c.getDeck() != null);

        Cell[] arr = new Cell[emptyCells.size()];
        return emptyCells.toArray(arr);
    }

    private Set<Cell> filterCells(Predicate<Cell> p) {
        // найти незанятые ячейки

        return new HashSet<>(cells.values());
    }

    public Set<Cell> getAllLockedCells() {
        return filterCells(c -> c.getState() == IShotableUnit.ShotableUnitState.Broken);
    }

    public boolean setShips(List<Ship> shipsPool) {
        // рандомным образом расставить полученные корабли
        // очистить все клетки от палуб кораблей
        for (Cell cell : cells.values()) {
            cell.unsetDeck();
        }
        // составить очередь из кораблей
        Deque<Ship> shipsQueue = new ArrayDeque<>();

        for (Ship ship : shipsPool) {
            shipsQueue.addFirst(ship);
        }
        // расставленные корабли
        List<Ship> settedShips = new ArrayList<>();

        int totalLimit = shipsPool.size() * 3;
        int shipLimit = 4 * 10;

        int totalIterations = 0;
        // расставлять флот, пока не получится или не выйдет лимит
        while (totalIterations < totalLimit) {
            totalIterations++;

            Ship currentShip = shipsQueue.pollFirst();
            if (currentShip == null) {
                // all ships are already disposed
                break;
            }

            boolean shipOk = false;

            int shipIterations = 0;
            // расставлять корабль, пока не получится или не выйдет лимит
            while (shipIterations < shipLimit) {
                shipIterations++;

                Cell freeCell = Utils.chooseOne(emptyCells());

                if (freeCell == null) {
                    shipOk = false;
                    break;
                }

                for (Direction dir : currentShip.meaningfulDirections()) {
                    // choose another
                    dir = Utils.chooseOne(currentShip.meaningfulDirections());
                    ShipLocation shipLoc = new ShipLocation(freeCell.getLocation(), dir);

                    // разместить корабль
                    shipOk = setShip(currentShip, shipLoc);
                    if (shipOk) {
                        break;
                    }
                }
                if (shipOk) {
                    // add to disposed
                    settedShips.add(currentShip);
                    break;
                }
            }

            // if error
            if (!shipOk) {
                totalIterations--;

                if (settedShips.isEmpty()) {
                    System.out.println("Cannot dispose ship: nothing to unset");
                    return false;
                }

                // unset a random ship
                // любого по индексу
                int index = (int) (Math.random() * settedShips.size());

                Ship shipToUnset = settedShips.get(index);
                settedShips.remove(index);

                unsetShip(shipToUnset);

                // return ships to queue
                shipsQueue.addLast(shipToUnset);
                shipsQueue.addLast(currentShip);
            }

        }
        ships = settedShips;
        //подключить наблюдеталей
        for (Ship ship : ships) {
            ship.addShipListener(shipObserver);
        }

        if (!shipsQueue.isEmpty()) {
            System.out.println("dispose ships failed, ships remaining: " + shipsQueue.size());
        }
        return totalIterations < totalLimit;

    }

    private boolean setShip(Ship ship, ShipLocation shipLocation) {
        boolean isOk = true;

        // по всем палубам корабля
        for (Deck deck : ship.getDecks()) {
            // абсолютная позиция палубы
            Location loc = deck.getShipBasedLocation()
                    .movedBy(shipLocation.location().x(), shipLocation.location().y())
                    .rotatedBy(shipLocation.location(), shipLocation.direction());

            if (!this.isLocationValid(loc)) {
                isOk = false;
                break;
            }

            Cell cell = cells.get(loc);

            // пересекается по соседям с чужими палубами
            Set<Cell> influencedCells = this.get8NeighboursFor(loc);
            influencedCells.add(cell);

            for (Cell c : influencedCells) {
                if (c.getDeck() != null && !ship.isMyDeck(c.getDeck())) {
                    isOk = false;
                    break;
                }
            }

            if (!isOk) {
                break;
            }

            deck.setCell(cell);
        }

        if (!isOk) {
            unsetShip(ship);
        }

        return isOk;
    }

    public Set<Cell> get8NeighboursFor(Location location) {
        Set<Cell> set = new HashSet<>();

        for (int j = -1; j <= 1; j += 1) {
            for (int i = -1; i <= 1; i += 1) {
                if (i == 0 && j == 0) {
                    continue;
                }

                Location loc = location.movedBy(i, j);

                if (this.isLocationValid(loc)) {
                    set.add(cells.get(loc));
                }
            }
        }

        return set;
    }

    public Set<Cell> get4NeighboursFor(Location location) {
        Set<Cell> set = new HashSet<>();

        Location loc;
        loc = location.movedBy(-1, 0);
        if (this.isLocationValid(loc)) {
            set.add(cells.get(loc));
        }
        loc = location.movedBy(0, -1);
        if (this.isLocationValid(loc)) {
            set.add(cells.get(loc));
        }
        loc = location.movedBy(0, 1);
        if (this.isLocationValid(loc)) {
            set.add(cells.get(loc));
        }
        loc = location.movedBy(1, 0);
        if (this.isLocationValid(loc)) {
            set.add(cells.get(loc));
        }

        return set;
    }

    private void unsetShip(Ship ship) {
        // по всем палубам корабля
        for (Deck deck : ship.getDecks()) {
            deck.unsetCell();
        }
    }

    public List<Ship> getShips() {
        //получить список кораблей на поле
        return ships;
    }

    public Map<Location, Cell> getCells() {
        return cells;
    }

    public int size() {
        return size;
    }

    public Cell cellAt(Location location) {
        return cells.get(location);
    }

    private void init(int fieldSize) {

        this.size = fieldSize;

        cells.clear();

        // fill with cells
        for (int j = 0; j < size; ++j) {
            for (int i = 0; i < size; ++i) {
                Location loc = new Location(i, j);
                Cell newCell = new Cell(loc);
                //теперь поле слушает эту ячейку

                newCell.addModelUnitListener(shotableUnitObserver);
                cells.put(loc, newCell);
            }
        }

    }

    public Cell getLastHitCell() {
        return lastHitCell;
    }

    public boolean isLocationValid(Location location) {
        return location.x() >= 0
                && location.y() >= 0
                && location.x() < size
                && location.y() < size;
    }
    // ---------------------- Порождает события -----------------------------

    ArrayList<IGameFieldListener> listeners = new ArrayList();

    public void addFieldListener(IGameFieldListener l) {
        listeners.add(l);
    }

    public void fireMyCellDamaged() {
        for (IGameFieldListener listener : listeners) {
            listener.myCellDamaged();
        }
    }

    public void fireMyShipDamaged() {
        for (IGameFieldListener listener : listeners) {
            listener.myShipDamaged();
        }
    }

    public void fireMyShipDrowned() {
        for (IGameFieldListener listener : listeners) {
            listener.myShipDrowned();
        }
    }

    void clear() {

    }

    private class ShotableUnitObserver implements IShotableUnitListener {

        @Override
        public void unitDamaged(IShotableUnit unit) {
            lastHitCell = (Cell) unit;
            fireMyCellDamaged();
        }
    }

    private class ShipObserver implements IShipListener {

        @Override
        public void shipDrowned(IShotableUnit lastHittedDeck) {
            lastHitCell = ((Deck) lastHittedDeck).getCell();
            fireMyShipDrowned();
        }

        @Override
        public void shipDamaged() {
            fireMyShipDamaged();
        }
    }

}
