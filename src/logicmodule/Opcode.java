package logicmodule;

import java.io.Serializable;

/**
 * @author Kerekes Zoltán
 */
public class Opcode implements Serializable {
	
	public enum Sender {
		GUI,
		LOGIC
	}
	
	public enum Action {
		SELECT_TRAIN_DECK,
		SELECT_TRAIN_CARD,
		SELECT_TICKET_DECK,
		SELECT_CLAIM_ROUTE,
		SELECT_THROW_TICKET_CARDS,
		UPDATE_YOUR_TURN_STARTED,
		UPDATE_YOUR_TURN_ENDED,
		UPDATE_PLAYER_INDEX,
		UPDATE_ROUTE_CLAIMED,
		UPDATE_TRAIN_DECK,
		UPDATE_UPFACE_TRAIN_CARDS,
		UPDATE_TICKET_DECK,
		UPDATE_GAIN_TRAIN_CARD,
		UPDATE_GAIN_TICKET_CARDS,
		UPDATE_LOOSE_TRAIN_CARD,
		UPDATE_LOOSE_TICKET_CARD,
		UPDATE_SCORE,
	}
	
	public static final int SERVER_INDEX = 1;
	private Sender sender;
	private int senderIndex;

	/**
	 * added by
	 * @author bs
	 */
	private int recipientID = -1000; //-1000 = send to all
	private Action action;
	private int i1, i2, i3, i4;
	private String s1, s2, s3, s4, s5;

	public Opcode(Sender sender, int senderIndex, Action action, 
			int i1, int i2, int i3, int i4, String s1, String s2, String s3, String s4, String s5) {
		this.sender = sender;
		this.senderIndex = senderIndex;
		this.action = action;
		this.i1 = i1;
		this.i2 = i2;
		this.i3 = i3;
		this.i4 = i4;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4 = s4;
		this.s5 = s5;
	}
	public Opcode(Sender sender, int senderIndex, Action action) {
		this(sender, senderIndex, action, 0, 0, 0, 0, "", "", "", "", "");
	}
	public Opcode(Sender sender, int senderIndex, Action action, int i1) {
		this(sender, senderIndex, action, i1, 0, 0, 0, "", "", "", "", "");
	}
	public Opcode(Sender sender, int senderIndex, Action action, String s1) {
		this(sender, senderIndex, action, 0, 0, 0, 0, s1, "", "", "", "");
	}
	public Opcode(Sender sender, int senderIndex, Action action, int i1, String s1) {
		this(sender, senderIndex, action, i1, 0, 0, 0, s1, "", "", "", "");
	}
	public Opcode(Sender sender, int senderIndex, Action action, String s1, String s2, String s3) {
		this(sender, senderIndex, action, 0, 0, 0, 0, s1, s2, s3, "", "");
	}
	
	
	public void setRecipientID(int id) {

		this.recipientID = id;

	}

	public int getRecipientID() {

		return recipientID;
	}

	
	public Sender getSender() {
		return sender;
	}

	public int getSenderIndex() {
		return senderIndex;
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

	public String getS5() {
		return s5;
	}

}
