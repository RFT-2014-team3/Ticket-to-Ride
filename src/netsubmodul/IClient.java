package netsubmodul;

import logicmodule.Opcode;
/**
 * @author bs
 */
public interface IClient {


	/**
	 * Connects to the specified ip on the spec. port.
	 * @param ip address of the server
	 * @param port the port where the server listens
	 * @return boolean value, true if the connection was successful
	 */
	boolean startClient(String ip, int port);
	
	/**
	 * Prototype function for sending whatever we want to send. Note:
	 * MockGameObject is a non-existent game object class for developing the network submodul.
	 * @param obj the gameobject we want to send
	 * @return boolean value, true if sending was successful
	 */
	boolean SendToAll(Opcode obj);

	/**
	 * Sending info to one of the clients. For ex. signaling to the next player.
	 *
	 * @param obj the message to send
	 * @param clientId the unique id of the client we want to send
	 * @return boolean true if sending was successful
	 */
	boolean SendToOne(Opcode obj, int clientId);
}
