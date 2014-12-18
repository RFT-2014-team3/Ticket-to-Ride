package netsubmodul;

import java.net.*;
import logicmodule.Opcode;
import logicmodule.OpcodeHandler;

/**
 * @author bs
 */
public interface IServer {


	/**
	 * The servers starts listening on the specified port.
	 * Note: asynchronous call, returns control to caller, the server listens on another thread.  
	 * @param port the server is listening on this port
	 * @return the ServerData objects contains info about the connection (ip, port, etc. - exact def. later)
	 */
	void startServer(int port);

	/**
	 * Stops the server.
	 */
	void stopServer();
	
	/**
	 * Returns the number of connected clients
	 * @return number of connected clients
	 */
	int GetConnectedClients();

	/**
	 * Returns the string representaion of the ip of the host machine
	 *
	 * @return IP of the host
	 */
	String GetServerAddress();
	
	/**
	 * The game begins, so the server stops listening, because we don't need new players.
	 * Call in the beginning of every new match.
	 */
	void StartMatch();
        
        /**
         * Send to every connected client.
         * @param msg The Message to send
         */
        void SendToAll(Opcode msg);
        
        /**
         * Sendto one specific client based on recipient ID in message
         * @param msg The message to Send
         */
        void SendToSpecificClient(Opcode msg);
        
        /**
         * True if the match is already started.
         * @return state of the match
         */
        boolean isMatchStarted();
        
        /**
         * 
         * @return True if the server is listening on the port.
         */
        boolean isListening();
        
        /**
         * 
         * @return True if the server is started.
         */
        boolean isServerRunning();
        
         /**
         * Setting the handler to decode incoming messages. We should refactor this
         * with dependency injection.
         * @param op the handler who will handle the incoming messages
         */
        void setOpcodeHandler(OpcodeHandler op);
}
