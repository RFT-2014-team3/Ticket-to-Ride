package logicmodule;

import shared.TrainColor;

/**
 * @author Kerekes Zoltán
 */
class TrainCardImpl implements TrainCard {

	TrainColor color;
	
	public TrainCardImpl(TrainColor color) {
		this.color = color;
	}

	@Override
	public TrainColor getColor() {
		return this.color;
	}

}
