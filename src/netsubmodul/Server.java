package netsubmodul;

import logicmodule.Opcode;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;


/**
 * @author bs
 */
public class Server extends NetComponent implements IServer {

	
	private static Server instance = new Server();

	private InetAddress serverAddress;
	List<ClientThread> connectedClients;
	private ServerSocket server = null;
	private Thread listenerThread;
	private boolean matchStarted;
	private boolean running;
	private boolean listening;

	private final int ServerID = 1;

	private Server() {
		
		GetAddressForLocalhost();
		connectedClients =  new ArrayList<>();

	}

	
	public static Server GetServer() {
		return instance;
	}


	public ServerData startServer(final int port) {
		
		try {
			this.listening = true;
			this.running = true;
			listenerThread = new Thread( new Runnable() {

				public void run() {
					ListenToConnections(port);
					
				}
				
			});

			listenerThread.setDaemon(true);
			listenerThread.start();




		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Clean up threads.
	 */
	public void stopServer() {

		if(listenerThread.isAlive()) {

			StopServerListening();
		}

		this.running = false;
		for(ClientThread ct : connectedClients) {

			ct.CloseConnection();
			try {
				ct.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public int GetConnectedClients() {
		
		return connectedClients.size();
	}

	public void StartMatch() {
		
		StopServerListening();
		matchStarted = true;

		for(ClientThread ct : connectedClients) {

			ct.setDaemon(true);
			ct.start();
		}
		
				
	}

	private void StopServerListening() {

		listening = false;
		try {
			if(server != null)
				server.close();

			listenerThread.join();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public InetAddress GetServerAddress() {
		return serverAddress;
	}

	public boolean isServerRunning() {

		return running;
	}

	private void GetAddressForLocalhost()
	{
		
		try {
			serverAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	private void ListenToConnections(int port) {
		
		int id = 2;

		try {
			 server = new ServerSocket(port);
			 
			while(!matchStarted && listening) {
				
				Socket clientSocket = server.accept();

				System.out.println(id);
				connectedClients.add(new ClientThread( clientSocket, id++ ));
			}
			
		} catch (IOException e) {
			
			//e.printStackTrace();
		}
		finally	{


			if(server != null) {
				try {
					server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
						
		}
				
	}

	public static void main(String args[]) {

		new Server().startServer(9999);

		/**
		 * Infinite loop for testing - startServer return immediately
		 */
		while(true) {}

	}

	private class ClientThread extends Thread {
	
		
		private Socket clSocket;
		private ObjectInputStream iStream;
		private ObjectOutputStream oStream;
		private int id;
		
		public ClientThread(Socket clientSocket, int id) {
			
			this.clSocket = clientSocket; 
			this.id = id;
			setupStreams();

		}
	
		
		private void setupStreams() {
			
			try {

				oStream =  new ObjectOutputStream (clSocket.getOutputStream());
				iStream =  new ObjectInputStream  (clSocket.getInputStream());

				oStream.writeObject( id );
			}catch (Exception ex) {
				
			}
			
		}
		
		public void run() {
				
				
			while(running) {

				try {

					Opcode msg = (Opcode) iStream.readObject();


					if(msg != null && msg.getRecipientID() == 1) {	//msg to the server
						msgHandler.decodeOpcode( msg );
					} else if(msg != null && msg.getRecipientID() == -1000) {

						msgHandler.decodeOpcode( msg );
						SendToAll(msg);
					} else {

						SendToSpecificClient(msg);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
				
			
		}

		public void CloseConnection() {
			
			try {
				iStream.close();
				oStream.close();
				clSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void Send(Object msg) throws IOException {


			oStream.writeObject(msg);

		}

		public int getClientThreadId() {

			return this.id;
		}


	}
	
	//TODO: make it nicer -> maybe one method for sending to everybody or one client only, deciding with a flag
	public void SendToAll(Opcode info) {


		synchronized (connectedClients) {

			ListIterator<ClientThread> listIterator = connectedClients.listIterator();

			while (listIterator.hasNext()) {

				ClientThread ct = listIterator.next();

				if(ct.getId() == info.getSenderID()) {	//?
					continue;
				}


				try {

					ct.Send(info);

				} catch (IOException e) {
					listIterator.remove();
				}

			}
		}

	}

	public void SendToSpecificClient(Opcode msg) {



		synchronized (connectedClients) {

			ListIterator<ClientThread> listIterator = connectedClients.listIterator();

			while (listIterator.hasNext()) {

				ClientThread ct = listIterator.next();

				if(ct.getId() == msg.getSenderID()) {	//?
					try {

						ct.Send(msg);

					} catch (IOException e) {
						listIterator.remove();
					}

				}


			}
		}

	}


}
