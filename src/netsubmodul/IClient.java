package netsubmodul;

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
	boolean SendData(MockGameObject obj);
	
	/**
	 * Sends a message to the server, server should notify the next player in this turn.
	 */
	void SignalNextPlayer();
}
