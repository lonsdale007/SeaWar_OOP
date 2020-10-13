package seawargame.model;

public abstract class Player {

    protected GameField enemyField;

    public void setField(GameField field){
        this.enemyField = field;
    }

    public GameField getEnemyField(){
        return this.enemyField;
    }
}
