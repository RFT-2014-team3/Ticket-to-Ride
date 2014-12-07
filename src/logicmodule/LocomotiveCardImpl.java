package logicmodule;

import shared.TrainColor;

/**
 * @author Kerekes Zoltán
 */
class LocomotiveCardImpl extends TrainCardImpl implements LocomotiveCard {

	public LocomotiveCardImpl(){
		super(TrainColor.GREY);
	}

}
