package seawargame.model;

import java.util.ArrayList;
import seawargame.util.Utils;

public class ShipPoolFactory {

    private ArrayList<Ship> allNonStandardShips = new ArrayList<>();


    ShipPoolFactory(){
        allNonStandardShips.add(new Submarine());
    }

    public ArrayList<Ship> createStandardShipPool(){
        ArrayList<Ship> standardShips= new ArrayList<>();
        standardShips.add(new FourDeckShip());

        standardShips.add(new ThreeDeckShip());
        standardShips.add(new ThreeDeckShip());

        standardShips.add(new TwoDeckShip());
        standardShips.add(new TwoDeckShip());
        standardShips.add(new TwoDeckShip());

        standardShips.add(new OneDeckShip());
        standardShips.add(new OneDeckShip());
        standardShips.add(new OneDeckShip());
        standardShips.add(new OneDeckShip());

        return standardShips;

    }

    public ArrayList<Ship> createNonStandardShipPool(){
        ArrayList <Ship> nonStandardShips = new ArrayList<>();
        for (int i=0; i < 10; i++){
            nonStandardShips.add((Ship)Utils.chooseOne(allNonStandardShips).clone() );
        }
        return nonStandardShips;
    }
}
