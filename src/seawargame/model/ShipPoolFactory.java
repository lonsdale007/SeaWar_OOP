package seawargame.model;

import java.util.ArrayList;
import java.util.Objects;

import seawargame.util.Utils;

public class ShipPoolFactory {

    private ArrayList<Ship> allNonStandardShips = new ArrayList<>();

    ShipPoolFactory(){
        allNonStandardShips.add(new Submarine());
    }

    public ArrayList<Ship> createStandardShipPool(){
        ArrayList<Ship> standardShips= new ArrayList<>();
        standardShips.add(new StandardShip(4));

        standardShips.add(new StandardShip(3));
        standardShips.add(new StandardShip(3));

        standardShips.add(new StandardShip(2));
        standardShips.add(new StandardShip(2));
        standardShips.add(new StandardShip(2));

        standardShips.add(new StandardShip(1));
        standardShips.add(new StandardShip(1));
        standardShips.add(new StandardShip(1));
        standardShips.add(new StandardShip(1));

        return standardShips;
    }

    public ArrayList<Ship> createNonStandardShipPool(){
        ArrayList <Ship> nonStandardShips = new ArrayList<>();
        for (int i=0; i < 10; i++){
            nonStandardShips.add(Objects.requireNonNull(Utils.chooseOne(allNonStandardShips)).clone());
        }
        return nonStandardShips;
    }
}
