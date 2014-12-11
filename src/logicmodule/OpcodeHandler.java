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
						con._drawTicketCards(opc.getSenderIndex());
						break;
						
					case SELECT_CLAIM_ROUTE:
						con._claimRoute(opc.getSenderIndex(), shared.Route.valueOf(opc.getS1()));
						break;
					case SELECT_THROW_TICKET_CARDS:
						List<shared.TicketCard> cards = new ArrayList<>();
						if(!opc.getS1().equals("")) 
							cards.add(shared.TicketCard.valueOf(opc.getS1()));
						if(!opc.getS2().equals("")) 
							cards.add(shared.TicketCard.valueOf(opc.getS2()));
						if(!opc.getS3().equals("")) 
							cards.add(shared.TicketCard.valueOf(opc.getS3()));
						con._throwTicketCards(opc.getSenderIndex(), cards);
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
						
					case UPDATE_GAIN_TRAIN_CARD:
						con._guiUpdateGainTrainCard(shared.TrainColor.valueOf(opc.getS1()));
						break;
						
					case UPDATE_GAIN_TICKET_CARDS:
						List<shared.TicketCard> cards2 = new ArrayList<>();
						if(!opc.getS1().equals("")) 
							cards2.add(shared.TicketCard.valueOf(opc.getS1()));
						if(!opc.getS2().equals("")) 
							cards2.add(shared.TicketCard.valueOf(opc.getS2()));
						if(!opc.getS3().equals("")) 
							cards2.add(shared.TicketCard.valueOf(opc.getS3()));
						con._guiUpdateGainTicketCards(cards2);
						break;
						
					case UPDATE_LOOSE_TRAIN_CARD:
						con._guiUpdateLooseTrainCard(shared.TrainColor.valueOf(opc.getS1()), opc.getI1());
						break;
						
					case UPDATE_LOOSE_TICKET_CARD:
						con._guiUpdateLooseTicketCard(shared.TicketCard.valueOf(opc.getS1()));
						break;
						
					case UPDATE_SCORE:
						con._guiUpdateScore(opc.getI1());
						break;
				}
				break;
		}
	}
	
}
