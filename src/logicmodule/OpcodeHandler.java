package logicmodule;

import java.util.ArrayList;
import java.util.List;
import shared.PlayerColor;
import shared.TicketCard;
import shared.TrainColor;

/**
 * @author Kerekes Zoltán
 */
public class OpcodeHandler implements NetworkHandler {
	
	private static OpcodeHandler instance = new OpcodeHandler();
	private Controller con = Controller.getInstance();
	
	public static OpcodeHandler getInstance() {
		return instance;
	}
	
	public void sendTo(int playerID, Opcode opcode) {
		if(con.isServer()) {
			opcode.setRecipientID(playerID);
			((netsubmodul.Server)con.server).SendToSpecificClient(opcode);
		} else {
			con.client.SendToOne(opcode, playerID);
		}
	}
	
	@Override
	public void decodeOpcode(Opcode opc) {
		switch (opc.getAction()) {
			case SELECT_TRAIN_DECK:
				con._drawTrainCard(opc.getSenderID());
				break;

			case SELECT_TRAIN_CARD:
				con._drawTrainCard(opc.getSenderID(), opc.getI1());
				break;

			case SELECT_TICKET_DECK:
				con._drawTicketCards(opc.getSenderID());
				break;

			case SELECT_CLAIM_ROUTE:
				con._claimRoute(opc.getSenderID(), shared.Route.valueOf(opc.getS1()));
				break;
			case SELECT_THROW_TICKET_CARDS:
				List<TicketCard> cards = new ArrayList<>();
				if(!opc.getS1().equals("")) 
					cards.add(TicketCard.valueOf(opc.getS1()));
				if(!opc.getS2().equals("")) 
					cards.add(TicketCard.valueOf(opc.getS2()));
				if(!opc.getS3().equals("")) 
					cards.add(TicketCard.valueOf(opc.getS3()));
				con._throwTicketCards(opc.getSenderID(), cards);

			// =====================================================

			case UPDATE_YOUR_TURN_STARTED:
				con._guiUpdateYourTurnStarted();
				break;

			case UPDATE_YOUR_TURN_ENDED:
				con._guiUpdateYourTurnEnded();
				break;

			case UPDATE_ROUTE_CLAIMED:
				con._guiUpdateRouteClaimed(shared.Route.valueOf(opc.getS1()), 
						opc.getS2().equals("") ? null : PlayerColor.valueOf(opc.getS2()),
						opc.getS3().equals("") ? null : PlayerColor.valueOf(opc.getS3()));
				break;

			case UPDATE_TRAIN_DECK:
				con._guiUpdateTrainDeck(opc.getI1());
				break;

			case UPDATE_UPFACE_TRAIN_CARDS:
				List<TrainColor> cards3 = new ArrayList<>();
				cards3.add(opc.getS1().equals("") ? null : TrainColor.valueOf(opc.getS1()));
				cards3.add(opc.getS2().equals("") ? null : TrainColor.valueOf(opc.getS2()));
				cards3.add(opc.getS3().equals("") ? null : TrainColor.valueOf(opc.getS3()));
				cards3.add(opc.getS4().equals("") ? null : TrainColor.valueOf(opc.getS4()));
				cards3.add(opc.getS5().equals("") ? null : TrainColor.valueOf(opc.getS5()));
				con._guiUpdateUpfaceTrainCards(cards3);
				break;

			case UPDATE_TICKET_DECK:
				con._guiUpdateTicketDeck(opc.getI1());
				break;

			case UPDATE_GAIN_TRAIN_CARD:
				con._guiUpdateGainTrainCard(TrainColor.valueOf(opc.getS1()));
				break;

			case UPDATE_GAIN_TICKET_CARDS:
				List<TicketCard> cards2 = new ArrayList<>();
				if(!opc.getS1().equals("")) 
					cards2.add(TicketCard.valueOf(opc.getS1()));
				if(!opc.getS2().equals("")) 
					cards2.add(TicketCard.valueOf(opc.getS2()));
				if(!opc.getS3().equals("")) 
					cards2.add(TicketCard.valueOf(opc.getS3()));
				con._guiUpdateGainTicketCards(cards2);
				break;

			case UPDATE_LOOSE_TRAIN_CARD:
				con._guiUpdateLooseTrainCard(TrainColor.valueOf(opc.getS1()), opc.getI1());
				break;

			case UPDATE_LOOSE_TICKET_CARD:
				con._guiUpdateLooseTicketCard(TicketCard.valueOf(opc.getS1()));
				break;

			case UPDATE_SCORE:
				con._guiUpdateScore(opc.getI1());
				break;
		}
	}
	
}
