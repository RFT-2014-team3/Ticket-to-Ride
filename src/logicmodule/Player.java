package logicmodule;

import shared.TicketCard;
import shared.PlayerColor;
import shared.TrainColor;
import java.util.List;

/**
 * @author Kerekes Zoltán
 */
interface Player {
	void addTrainCards(TrainColor color, int n);
	List<TrainCard> removeTrainCards(TrainColor color, int n);
	int getTrainCardCount(TrainColor color);
	List<TicketCard> getTicketCards();
	int getRemainingTrainsCount();
	void setRemainingTrainsCount(int newValue);
	PlayerColor getColor();
	void setColor(PlayerColor color);
	int getScore();
	void addScore(int diff);
}
