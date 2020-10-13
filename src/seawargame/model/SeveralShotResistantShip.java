package seawargame.model;

public abstract class SeveralShotResistantShip extends Ship {

    SeveralShotResistantShip(){
        super();
        init();
    }
    @Override
    public Direction[] meaningfulDirections() {
        return new Direction [] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST} ;
    }
}
