package logicmodule;

import shared.PlayerColor;

/**
 * @author Kerekes Zoltán
 */
class ScoreMarkerImpl implements ScoreMarker {

	int markedScore = 0;
	Player owner;
	
	public ScoreMarkerImpl(Player owner) {
		this.owner = owner;
	}

	@Override
	public Player getOwner() {
		return owner;
	}

	@Override
	public void stepForward() {
		markedScore++;
		// TODO: [logic] animáció jelzése a GUI modul felé.
	}
	
	@Override
	public void stepBackward() {
		markedScore--;
		// TODO: [logic] animáció jelzése a GUI modul felé.
	}
	@Override
	public int getMarkedScore() {
		return markedScore;
	}

	@Override
	public PlayerColor getColor() {
		return owner.getColor();
	}

}
