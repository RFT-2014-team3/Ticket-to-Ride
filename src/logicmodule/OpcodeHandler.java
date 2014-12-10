package logicmodule;

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
		// TODO: [logic] hálózati modulon keresztül küldés
	}
	
	@Override
	public void decodeOpcode(Opcode opc) {
		switch (opc.getSender()) {
			case GUI:
				switch (opc.getAction()) {
					case SELECT_TRAIN_DECK:
						con._drawTrainCard(opc.getPlayerIndex());
						break;
					case SELECT_TRAIN_CARD:
						con._drawTrainCard(opc.getPlayerIndex(), opc.getI1());
						break;
					case SELECT_TICKET_DECK:
						con._drawTicketCard(opc.getPlayerIndex());
						break;
					case SELECT_CLAIM_ROUTE:
						con._claimRoute(opc.getPlayerIndex(), shared.Route.valueOf(opc.getS1()));
						break;
				}
				break;
				
			case LOGIC:
				// TODO [logic] kb ugyanennyi jön majd ide: a szerver logikai modulja küldi az infót az egyik kliens GUI-jának
				break;
		}
	}
	
}
