package seawargame.model;

public class StandardShip extends Ship{
    public StandardShip(){
        super();
    }
    private StandardShip(StandardShip other){
        this();
    }
    protected int length;

    /**
     * @param field
     * @param length number of decks
     */
    public StandardShip(GameField field, int length) {
        super();

        if(length < 1) {
            throw new RuntimeException("Invalid length for linear ship is specified: "+length);
        }
        this.length = length;

        init();
    }
    public StandardShip(int length) {
        super();

        if(length < 1) {
            throw new RuntimeException("Invalid length for linear ship is specified: "+length);
        }
        this.length = length;

        init();
    }

    @Override
    protected void init() {

        for( int i = 0 ; i < this.length ; i++ ) {
            Deck deck = new StandardDeck(this, new Location(i,0));
            //теперь корабль следит за этой палубой//
            deck.addModelUnitListener(shipObserver);
            this.decks.add( deck);

        }
    }

    // для стандартных кораблей только 2 направления
    @Override
    public Direction[] meaningfulDirections() {
        return new Direction [] {Direction.NORTH, Direction.EAST} ;
    }

    @Override
    public Ship clone() {
        return new StandardShip(this);
    }
}
