package seawargame.model.events;

import seawargame.model.GameModel;
import seawargame.model.Player;
import seawargame.model.ShootResult;

public interface IGameListener {
    void shootPerformed(ShootResult shootResult);

    void gameFinished(GameModel.GameResult result);

    void exchangePlayer(Player activePlayer);
}
