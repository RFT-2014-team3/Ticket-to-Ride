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
	
	private static final OpcodeHandler instance = new OpcodeHandler();
	
	public static OpcodeHandler getInstance() {
		return instance;
	}
	
	private OpcodeHandler() {}
	
	public void sendTo(int playerID, Opcode opcode) {
		Controller con = Controller.getInstance();
		if(con.isServer()) {
			opcode.setRecipientID(playerID);
			((netsubmodul.Server)con.server).SendToSpecificClient(opcode);
		} else {
			con.client.SendToOne(opcode, playerID);
		}
	}
	
	@Override
	public void decodeOpcode(Opcode opc) {
		Controller con = Controller.getInstance();
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
				break;

			case SELECT_COLOR:
				con._chooseColor(opc.getSenderID(), shared.PlayerColor.valueOf(opc.getS1()));
				break;
				
			// =====================================================

			case UPDATE_COLOR_CHOSE:
				con._clientUpdateColorChose(opc.getI1(), shared.PlayerColor.valueOf(opc.getS1()));
				break;
				
			case UPDATE_INIT_TURN_END:
				con._clientUpdateInitTurnEnd();
				break;
				
			case UPDATE_GAME_FINISHED:
				con._clientUpdateGameFinished();
				break;
				
			case UPDATE_PLAYER_COUNT:
				con._clientUpdatePlayerCount(opc.getI1());
				break;
				
			case UPDATE_GAME_STARTED:
				con._clientUpdateGameStarted();
				break;
				
			case UPDATE_YOUR_TURN_STARTED:
				con._clientUpdateYourTurnStarted();
				break;

			case UPDATE_YOUR_TURN_ENDED:
				con._clientUpdateYourTurnEnded();
				break;

			case UPDATE_ROUTE_CLAIMED:
				con._clientUpdateRouteClaimed(shared.Route.valueOf(opc.getS1()), 
						opc.getS2().equals("") ? null : PlayerColor.valueOf(opc.getS2()),
						opc.getS3().equals("") ? null : PlayerColor.valueOf(opc.getS3()));
				break;

			case UPDATE_TRAIN_DECK:
				con._clientUpdateTrainDeck(opc.getI1());
				break;

			case UPDATE_UPFACE_TRAIN_CARDS:
				List<TrainColor> cards3 = new ArrayList<>();
				cards3.add(opc.getS1().equals("") ? null : TrainColor.valueOf(opc.getS1()));
				cards3.add(opc.getS2().equals("") ? null : TrainColor.valueOf(opc.getS2()));
				cards3.add(opc.getS3().equals("") ? null : TrainColor.valueOf(opc.getS3()));
				cards3.add(opc.getS4().equals("") ? null : TrainColor.valueOf(opc.getS4()));
				cards3.add(opc.getS5().equals("") ? null : TrainColor.valueOf(opc.getS5()));
				con._clientUpdateUpfaceTrainCards(cards3);
				break;

			case UPDATE_TICKET_DECK:
				con._clientUpdateTicketDeck(opc.getI1());
				break;

			case UPDATE_GAIN_TRAIN_CARD:
				con._clientUpdateGainTrainCard(TrainColor.valueOf(opc.getS1()));
				break;

			case UPDATE_GAIN_TICKET_CARDS:
				List<TicketCard> cards2 = new ArrayList<>();
				if(!opc.getS1().equals("")) 
					cards2.add(TicketCard.valueOf(opc.getS1()));
				if(!opc.getS2().equals("")) 
					cards2.add(TicketCard.valueOf(opc.getS2()));
				if(!opc.getS3().equals("")) 
					cards2.add(TicketCard.valueOf(opc.getS3()));
				con._clientUpdateGainTicketCards(cards2);
				break;

			case UPDATE_LOOSE_TRAIN_CARD:
				con._clientUpdateLooseTrainCard(TrainColor.valueOf(opc.getS1()), opc.getI1());
				break;

			case UPDATE_LOOSE_TICKET_CARD:
				con._clientUpdateLooseTicketCard(TicketCard.valueOf(opc.getS1()));
				break;

			case UPDATE_SCORE:
				List<Integer> scores = new ArrayList<>();
				scores.add(opc.getI1());
				scores.add(opc.getI2());
				scores.add(opc.getI3());
				scores.add(opc.getI4());
				scores.add(opc.getI5());
				con._clientUpdateScores(scores);
				break;
		}
	}
	
}
