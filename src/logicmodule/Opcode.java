package logicmodule;

/**
 * @author Kerekes Zoltán
 */
public class Opcode {
	
	public enum Sender {
		GUI,
		LOGIC
	}
	
	public enum Action {
		SELECT_TRAIN_DECK,
		SELECT_TRAIN_CARD,
		SELECT_TICKET_DECK,
		SELECT_CLAIM_ROUTE,
	}
	
	public static final int SERVER_INDEX = 1;
	private Sender sender;
	private int playerIndex;
	private Action action;
	private int i1, i2, i3, i4;
	private String s1, s2, s3, s4;

	public Opcode(Sender sender, int playerIndex, Action action, 
			int i1, int i2, int i3, int i4, String s1, String s2, String s3, String s4) {
		this.sender = sender;
		this.playerIndex = playerIndex;
		this.action = action;
		this.i1 = i1;
		this.i2 = i2;
		this.i3 = i3;
		this.i4 = i4;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4 = s4;
	}
	public Opcode(Sender sender, int playerIndex, Action action) {
		this(sender, playerIndex, action, 0, 0, 0, 0, "", "", "", "");
	}
	public Opcode(Sender sender, int playerIndex, Action action, int i1) {
		this(sender, playerIndex, action, i1, 0, 0, 0, "", "", "", "");
	}
	public Opcode(Sender sender, int playerIndex, Action action, String s1) {
		this(sender, playerIndex, action, 0, 0, 0, 0, s1, "", "", "");
	}
	
	
	
	public Sender getSender() {
		return sender;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public Action getAction() {
		return action;
	}

	public int getI1() {
		return i1;
	}

	public int getI2() {
		return i2;
	}

	public int getI3() {
		return i3;
	}

	public int getI4() {
		return i4;
	}

	public String getS1() {
		return s1;
	}

	public String getS2() {
		return s2;
	}

	public String getS3() {
		return s3;
	}

	public String getS4() {
		return s4;
	}

}
