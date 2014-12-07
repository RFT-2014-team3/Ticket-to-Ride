package logicmodule;

import shared.PlayerColor;

/**
 * @author Kerekes Zoltán
 */
interface ScoreMarker {
	Player getOwner();
	void stepForward();
	void stepBackward();
	int getMarkedScore();
	PlayerColor getColor();
}
