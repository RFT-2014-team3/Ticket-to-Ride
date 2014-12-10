package logicmodule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kerekes Zoltán
 */
public class OpcodeHandler implements NetworkHandler {
	
	private static OpcodeHandler instance = new OpcodeHandler();
	private Controller con = Controller.getInstance();
	
	public static OpcodeHandler getInstance() {
		return instance;
	}
	
	public void sendOpcodeTo(int playerIndex, Opcode opcode) {
		// TODO [logic] hálózati modulon keresztül küldés
	}
	
	@Override
	public void decodeOpcode(Opcode opc) {
		switch (opc.getSender()) {
			case GUI:
				switch (opc.getAction()) {
					case SELECT_TRAIN_DECK:
						con._drawTrainCard(opc.getSenderIndex());
						break;
						
					case SELECT_TRAIN_CARD:
						con._drawTrainCard(opc.getSenderIndex(), opc.getI1());
						break;
						
					case SELECT_TICKET_DECK:
						con._drawTicketCard(opc.getSenderIndex());
						break;
						
					case SELECT_CLAIM_ROUTE:
						con._claimRoute(opc.getSenderIndex(), shared.Route.valueOf(opc.getS1()));
						break;
				}
				break;
				
			case LOGIC:
				switch (opc.getAction()) {
					case UPDATE_YOUR_TURN_STARTED:
						con._guiUpdateYourTurnStarted();
						break;
						
					case UPDATE_YOUR_TURN_ENDED:
						con._guiUpdateYourTurnEnded();
						break;
						
					case UPDATE_PLAYER_INDEX:
						con._updatePlayerIndex(opc.getI1());
						break;
						
					case UPDATE_ROUTE_CLAIMED:
						con._guiUpdateRouteClaimed(shared.Route.valueOf(opc.getS1()), 
								opc.getS2().equals("") ? null : shared.PlayerColor.valueOf(opc.getS2()),
								opc.getS3().equals("") ? null : shared.PlayerColor.valueOf(opc.getS3()));
						break;
						
					case UPDATE_TRAIN_DECK:
						con._guiUpdateTrainDeck(opc.getI1());
						break;
						
					case UPDATE_UPFACE_TRAIN_CARDS:
						List<shared.TrainColor> cards = new ArrayList<>();
						cards.add(opc.getS1().equals("") ? null : shared.TrainColor.valueOf(opc.getS1()));
						cards.add(opc.getS2().equals("") ? null : shared.TrainColor.valueOf(opc.getS2()));
						cards.add(opc.getS3().equals("") ? null : shared.TrainColor.valueOf(opc.getS3()));
						cards.add(opc.getS4().equals("") ? null : shared.TrainColor.valueOf(opc.getS4()));
						cards.add(opc.getS5().equals("") ? null : shared.TrainColor.valueOf(opc.getS5()));
						con._guiUpdateUpfaceTrainCards(cards);
						break;
						
					case UPDATE_TICKET_DECK:
						con._guiUpdateTicketDeck(opc.getI1());
						break;
				}
				break;
		}
	}
	
}
