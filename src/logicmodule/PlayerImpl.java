package logicmodule;

import java.util.ArrayList;
import java.util.List;
import shared.PlayerColor;
import shared.TicketCard;

/**
 * @author Kerekes Zoltán
 */
class PlayerImpl implements Player {

	private List<TrainCard> trainCards;
	private List<TicketCard> ticketCards;
	private int remainingTrains = 45;
	private ScoreMarker scoreMarker;
	private int score = 0;
	private PlayerColor color;
	
	public PlayerImpl(ScoreMarker scoreMarker) {
		this.scoreMarker = scoreMarker;
		trainCards = new ArrayList<>();
		ticketCards = new ArrayList<>();
	}

	@Override
	public List<TrainCard> getTrainCards() {
		return trainCards;
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
		if(diff > 0){
			while(scoreMarker.getMarkedScore() < score)
				scoreMarker.stepForward();
		} else if(diff < 0) {
			while(scoreMarker.getMarkedScore() > score)
				scoreMarker.stepBackward();
		}
	}

}
