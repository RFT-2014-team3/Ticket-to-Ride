package logicmodule;

import shared.TicketCard;
import shared.PlayerColor;
import java.util.List;

/**
 * @author Kerekes Zoltán
 */
interface Player {
	List<TrainCard> getTrainCards();
	List<TicketCard> getTicketCards();
	int getRemainingTrainsCount();
	void setRemainingTrainsCount(int newValue);
	PlayerColor getColor();
	void setColor(PlayerColor color);
	int getScore();
	void addScore(int diff);
}
