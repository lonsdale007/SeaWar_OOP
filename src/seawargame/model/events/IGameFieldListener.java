package seawargame.model.events;

public interface IGameFieldListener {
    void myCellDamaged();

    void myShipDamaged();

    void myShipDrowned();
}
