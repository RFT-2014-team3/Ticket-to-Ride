package logicmodule;

import shared.TrainColor;

/**
 * @author Kerekes Zoltán
 */
interface Rail {
	TrainColor getColor();
	Player getOwner();
	void setOwner(Player owner);
}
