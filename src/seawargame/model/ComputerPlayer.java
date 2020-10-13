package seawargame.model;

import java.util.HashSet;
import java.util.Set;
import seawargame.model.events.IGameFieldListener;
import seawargame.model.events.IGameListener;
import seawargame.model.events.IShotableUnitListener;
import seawargame.util.Utils;


public class ComputerPlayer extends Player {

    private Set<Cell> allCells;
    private Set<Cell> cellsToShot;
    private Cell lastHurtedDeck;

    private GameModel game;
    private GameModelObserver observer;
    private UnitsObserver unitObserver;

    public ComputerPlayer() {
        cellsToShot = new HashSet<>();
        allCells = new HashSet<>();
        game = null;
        observer = new GameModelObserver();
        unitObserver = new UnitsObserver();
        lastHurtedDeck = null;
    }

    public ComputerPlayer(GameModel game) {
        this();
        this.game = game;
        this.game.addGameListener(observer);
        this.observer.setComputerPlayer(this);
    }

    @Override
    public void setField(GameField enemyField) {
        super.setField(enemyField);
        allCells.addAll(this.enemyField.getCells().values());

        for (Cell c : allCells) {
            c.addButtonUnitListener(unitObserver);
        }
    }

    public void shoot() {
        Cell nextCell;
        do {
            allCells.removeIf((c) -> c.getState() == IShotableUnit.ShotableUnitState.Broken);
            cellsToShot.retainAll(allCells);
            //выбрать клетку для выстрела
            nextCell = chooseCell(enemyField);
            System.out.println("nextcell= " + nextCell.getLocation().x() + " " + nextCell.getLocation().y() + "cellsToShot.size= " + cellsToShot.size());
            if (nextCell.getDeck() != null) {
                //запомнить клетку с палубой, в которую будем стрелять как последнюю подбитую
                lastHurtedDeck = nextCell;
                //сформировать множество клеток соседей, возможно содержащих еще палубу
                cellsToShot = enemyField.get8NeighboursFor(lastHurtedDeck.getLocation());
                //сама палуба тоже может быть не добита
                cellsToShot.add(lastHurtedDeck);
                allCells.removeIf(c -> (c.getDeck() !=null && c.getDeck().getState() == IShotableUnit.ShotableUnitState.Broken));
                //перед выстрелом исключить уже убитые клетки
                cellsToShot.retainAll(allCells);
                if (cellsToShot.contains(nextCell)){
                    nextCell.getDeck().shootAction();
                }
                allCells.removeIf(c -> (c.getDeck() !=null && c.getDeck().getState() == IShotableUnit.ShotableUnitState.Broken));
            } else {
                if (allCells.contains(nextCell)){
                    nextCell.shootAction();
                }
            }
        } while (nextCell.getDeck() != null);

    }

    public Cell chooseCell(GameField field) {
        Cell nextCell;
        Cell arr[] = {};
        if (lastHurtedDeck == null || cellsToShot.isEmpty()) {
            //рандом
            nextCell = Utils.chooseOne(allCells.toArray(arr));
        } else {
            nextCell = Utils.chooseOne(cellsToShot.toArray(arr));
        }
        return nextCell;

    }

    private class GameModelObserver implements IGameListener {

        ComputerPlayer player;

        public void setComputerPlayer(ComputerPlayer player) {
            this.player = player;
        }

        @Override
        public void shootPerformed(ShootResult shootResult) {

        }

        @Override
        public void gameFinished(GameModel.GameResult result) {

        }

        @Override
        public void exchangePlayer(Player activePlayer) {
            if (activePlayer instanceof ComputerPlayer && activePlayer == player) {
                shoot();
            }
        }

    }

    private class UnitsObserver implements IShotableUnitListener {

        @Override
        public void unitDamaged(IShotableUnit unit) {
            if (unit instanceof Deck && unit.getState() == IShotableUnit.ShotableUnitState.Broken) {
                allCells.remove(((Deck) unit).getCell());
            }
        }

    }

    private class GameFieldObserver implements IGameFieldListener {

        @Override
        public void myCellDamaged() {

            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void myShipDamaged() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void myShipDrowned() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
