package logicmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import shared.PlayerColor;
import shared.TicketCard;
import shared.TrainColor;

/**
 * @author Kerekes Zoltán
 */
class PlayerImpl implements Player {

	private Map<TrainColor, Integer> trainCards;
	private List<TicketCard> ticketCards;
	private int remainingTrains = 45;
	private int score = 0;
	private PlayerColor color;
	
	public PlayerImpl() {
		trainCards = new HashMap<>();
		for (TrainColor trainColor : TrainColor.values()) {
			trainCards.put(trainColor, new Integer(0));
		}
		ticketCards = new ArrayList<>();
	}

	@Override
	public void addTrainCards(TrainColor color, int n) {
		trainCards.put(color, trainCards.get(color) + n);
	}

	@Override
	public List<TrainCard> removeTrainCards(TrainColor color, int n) {
		trainCards.put(color, trainCards.get(color) - n);
		List<TrainCard> ret = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			ret.add(Factory.newTrainCard(color));
		}
		return ret;
	}

	@Override
	public int getTrainCardCount(TrainColor color) {
		return trainCards.get(color);
	}

	@Override
	public List<TicketCard> getTicketCards() {
		return ticketCards;
	}

	@Override
	public int getRemainingTrainsCount() {
		return remainingTrains;
	}

	@Override
	public void setRemainingTrainsCount(int newValue) {
		remainingTrains = newValue;
	}

	@Override
	public PlayerColor getColor() {
		return color;
	}

	@Override
	public void setColor(PlayerColor color) {
		this.color = color;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void addScore(int diff) {
		score += diff;
	}

}
