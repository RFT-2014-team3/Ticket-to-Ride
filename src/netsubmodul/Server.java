package netsubmodul;

import logicmodule.Opcode;
import logicmodule.OpcodeHandler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author bs
 */
public class Server implements IServer{

	
	private static Server instance = new Server();
	private InetAddress serverAddress;
	List<ClientThread> connectedClients;
	private ServerSocket server = null;
	
	private Thread listenerThread;
	private boolean matchStarted;
	private OpcodeHandler msgHandler;



	private Server() {
		
		GetAddressForLocalhost();
		connectedClients =  new ArrayList<>();
		

	}

	
	public static Server GetServer() {
		return instance;
	}


	public void SetMessageHandler(OpcodeHandler handler) {

		instance.msgHandler = handler;
	}

	public ServerData StartServer(final int port) {
		
		try {
			
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
	public void StopServer() {
		
		for(ClientThread ct : connectedClients) {
			
			ct.setIsGameRunning(false);
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
		
		matchStarted = true;
		
		try {
			server.close();
			listenerThread.join();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Here: " + connectedClients.size());
		for(ClientThread ct : connectedClients) {
			
			ct.setIsGameRunning(true);
			ct.setDaemon(true);
			ct.start();
		}
		
				
	}
	
	
	public static void main(String args[])
	{
		
		Server server = new Server();
		server.StartServer(9999);
		
		while(server.listenerThread.isAlive()){}
		
		
		boolean gameRunning = true;
		
		while(gameRunning) {
			gameRunning = false;
			for(ClientThread clientThread : server.connectedClients ) {
				if(clientThread.isAlive())
					gameRunning = true;
			}
		}
		/*
			try {
				Thread.sleep(5000);
				server.StartMatch();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			*/	
	}

	public InetAddress GetServerAddress() {
		return serverAddress;
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
		
				
		try {
			 server = new ServerSocket(port);
			 
			while(!matchStarted) {
				
				Socket clientSocket = server.accept(); 
				
				
				connectedClients.add(new ClientThread(clientSocket));
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
	
	
	private class ClientThread extends Thread {
	
		
		private Socket clSocket;
		private boolean playing;
		private ObjectInputStream iStream;
		private ObjectOutputStream oStream;

		
		public ClientThread(Socket clientSocket) {
			
			this.clSocket = clientSocket; 
		
			setupStreams();
		}
	
		
		private void setupStreams() {
			
			try {
				
				iStream =  new ObjectInputStream  (clSocket.getInputStream());
				oStream =  new ObjectOutputStream (clSocket.getOutputStream());
				
			}catch (Exception ex) {
				
			}
			
		}
		
		public void run() {
				
				
			while(playing) {
								
				try {

					Opcode msg = (Opcode) iStream.readObject();

					if(msg != null) {

						ParseMessage( msg );
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
								
			}
				
			
		}
		
		
		public void setIsGameRunning(boolean gameState) {
			this.playing = gameState;
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
		
		public void Send(Opcode msg) {

			try {
				oStream.writeObject((Object)msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	public void SendToAll(Opcode info) {
		
		for(ClientThread ct : connectedClients) {
			ct.Send(info);
		}
	}

	private void ParseMessage(Opcode msg) {

		if(this.msgHandler != null) {

			msgHandler.decodeOpcode(msg);
		}

	}
}
