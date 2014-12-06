package logicmodule;

/**
 * @author Kerekes Zoltán
 */
interface ScoreMarker {
	Player getOwner();
	void stepOne();
	int getMarkedScore();
}
