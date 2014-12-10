package netsubmodul;

import java.net.*;

public interface IServer {


	/**
	 * 
	 * Use this to get server reference. The design of the
	 * server class follows the singleton pattern, don't try to instantiate it. 
	 * @return Server server instance, singleton
	 */
	Server GetServer();
	
	/**
	 * The servers starts listening on the specified port.
	 * Note: asynchronous call, returns control to caller, the server listens on another thread.  
	 * @param port the server is listening on this port
	 * @return the ServerData objects contains info about the connection (ip, port, etc. - exact def. later)
	 */
	ServerData StartServer(int port);

	/**
	 * Stops the server.
	 */
	void StopServer();
	
	/**
	 * Returns the number of connected clients
	 * @return number of connected clients
	 */
	int GetConnectedClients();
	
	InetAddress GetServerAddress();
	
	/**
	 * The game begins, so the server stops listening, because we don't need new players.
	 * Call in the beginning of every new match.
	 */
	void StartMatch();
}
