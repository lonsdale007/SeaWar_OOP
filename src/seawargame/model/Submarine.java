package seawargame.model;

public class Submarine extends SeveralShotResistantShip {
    public Submarine() {
        super();
    }

    private Submarine(Submarine other) {
        this();
    }

    @Override
    protected void init() {
        Deck deck;

        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                deck = new TransparentDeck(this, new Location(i, 0));
                this.decks.add(deck);
            } else {
                deck = new StandardDeck(this, new Location(i, 0));
                this.decks.add(deck);
            }
            //теперь корабль следит за этой палубой//
            deck.addModelUnitListener(shipObserver);

        }
    }

    @Override
    public Ship clone() {
        return new Submarine(this);
    }
}
