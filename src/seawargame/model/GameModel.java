package seawargame.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seawargame.model.events.IGameFieldListener;
import seawargame.model.events.IGameListener;

public class GameModel {

    private boolean running;

    private GameField fields[] = {null, null};

    private Player players[] = {null, null}; //0 - bot, 1 - игрок

    private Player activePlayer;

    private List<List<Ship>> ships;

    private GameFieldObserver fieldObserver;

    private ShipPoolFactory shipPoolFactory;

    public enum GameResult {
        Victory, Losing, Ok
    }

    public GameModel() {
        this.running = false;
        this.fieldObserver = new GameFieldObserver();
        this.shipPoolFactory = new ShipPoolFactory();
        this.ships = new ArrayList<List<Ship>>();
    }

    public GameField[] getFields() {
        return fields;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public boolean startNewGame(boolean force, int gameMode, int fieldsSize) {

        if (running && !force) {
            return false;
        }
        if (running && force) {
            stopGame(true);
        }
        initBattleFields(fieldsSize);

        initPlayers();

        if (force) {
            ships.clear();
        }

        if (gameMode == 0) {
            for (int i = 0; i < 2; i++) {
                ships.add(shipPoolFactory.createStandardShipPool());
            }
        } else {
            for (int i = 0; i < 2; i++) {
                ships.add(shipPoolFactory.createNonStandardShipPool());
            }
        }

        //очистить поля от кораблей
        for (int i = 0; i < 2; i++) {
            fields[i].clear();
        }
        //расставить корабли на поле
        for (int i = 0; i < 2; i++) {
            if (!fields[i].setShips(ships.get(i))) {
                //не получилось разместить корабли, надо увеличить размер поля!
                int a = 0;
            }
        }

        players[0].setField(fields[1]);
        players[1].setField(fields[0]);

        running = true;
        return true;
    }

    private void initBattleFields(int size) {
        for (int i = 0; i < 2; i++) {
            GameField newField = new GameField(size);
            newField.addFieldListener(fieldObserver);
            fields[i] = newField;

        }
    }

    private void initPlayers() {
        Player newPlayer = new ComputerPlayer(this);
        players[0] = newPlayer;

        newPlayer = new HumanPlayer();
        players[1] = newPlayer;

    }

    private void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
        exchangePlayer(activePlayer);


    }

    public void runGame() {
        // to highlight views
        setActivePlayer(players[1]);
        exchangePlayer(players[1]);
    }

    private void identifyGameOver() {
        if (!fields[0].hasActiveShips()) {
            //если у робота не осталось активных кораблей
            fireGameFinished(GameResult.Victory);
        } else if (!fields[1].hasActiveShips()) {
            ////если у человека не осталось активных кораблей
            fireGameFinished(GameResult.Losing);
        }
    }

    private void handleShoot(ShootResult shootResult) {
        if (!running) {
            return;
        }
        // оповестить слушателей о событии
        fireShootPerfomed(shootResult);

        switch (shootResult) {
            case SHIP_DAMAGED:
                //ни на что не влияет
                break;
            case MISS:
                this.activePlayer = getInactivePlayer(activePlayer);
                // переход хода
                exchangePlayer(activePlayer);
                break;

            case SHIP_DROWNED:
                openCellsNearDrownedShip();
                identifyGameOver();
                break;
        }

    }

    private Player getInactivePlayer(Player activePlayer) {
        if (players[0] == activePlayer) {
            return players[1];
        } else {
            return players[0];
        }
    }

    private void openCellsNearDrownedShip() {

        GameField field = activePlayer.getEnemyField();

        Cell rememberedLastHitCell = field.getLastHitCell();

        Set<Cell> shipCells = new HashSet<>();
        shipCells.add(rememberedLastHitCell);
        Set<Cell> shipNeighbourCells = new HashSet<>();
        Set<Cell> neighbours;

        neighbours = field.get4NeighboursFor(rememberedLastHitCell.getLocation());

        while (!neighbours.isEmpty()) {
            neighbours.removeIf(c -> c.getDeck() == null);
            neighbours.removeAll(shipCells);

            shipCells.addAll(neighbours);

            shipNeighbourCells.removeAll(shipCells);
            shipNeighbourCells.addAll(neighbours); // новые в корабле

            neighbours.clear();
            for (Cell c : shipNeighbourCells) {
                neighbours.addAll(field.get4NeighboursFor(c.getLocation()));
            }
        }

        shipNeighbourCells.clear();

        // все клетки корабля найдены
        for (Cell c : shipCells) {
            shipNeighbourCells.addAll(field.get8NeighboursFor(c.getLocation()));
        }

        shipNeighbourCells.retainAll(field.getAllLockedCells());

        for (Cell c : shipNeighbourCells) {
            c.openAction();
        }

        // возвращаем выделение
        rememberedLastHitCell.openAction();
    }

    public boolean stopGame(boolean force) {
        if (running && !force) {
            return false;
        }
        running = false;

        return true;
    }

    private class GameFieldObserver implements IGameFieldListener {

        @Override
        public void myCellDamaged() {
            handleShoot(ShootResult.MISS);
        }

        @Override
        public void myShipDamaged() {
            handleShoot(ShootResult.SHIP_DAMAGED);
        }

        @Override
        public void myShipDrowned() {
            handleShoot(ShootResult.SHIP_DROWNED);
        }

    }

    ArrayList<IGameListener> gameListeners = new ArrayList();

    public void addGameListener(IGameListener l) {
        gameListeners.add(l);
    }

    public void deleteGameListener(IGameListener l) {
        gameListeners.remove(l);
    }

    public void fireShootPerfomed(ShootResult shootResult) {
        for (IGameListener gameListener : gameListeners) {
            gameListener.shootPerformed(shootResult);
        }
    }

    public void fireGameFinished(GameResult result) {
        for (IGameListener gameListener : gameListeners) {
            gameListener.gameFinished(result);
        }
    }

    public void exchangePlayer(Player activePlayer) {
        for (IGameListener gameListener : gameListeners) {
            gameListener.exchangePlayer(activePlayer);
        }
    }
}
