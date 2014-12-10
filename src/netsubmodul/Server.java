package netsubmodul;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


public class Server implements IServer{

	
	private static Server instance = new Server();
	private InetAddress serverAddress;
	List<ClientThread> connectedClients;
	private ServerSocket server = null;
	
	private Thread listenerThread;
	private boolean matchStarted;
	
	
	private Server() {
		
		GetAddressForLocalhost();
		connectedClients =  new ArrayList<ClientThread>();
		
	//	communicator = new JSONCommunicator();
	}
	
	
	public Server GetServer() {
		return instance;
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
		private JsonReader iStream;
		private JsonWriter oStream;
		private Gson parser;
		private boolean playing;
		
		public ClientThread(Socket clientSocket) {
			
			this.clSocket = clientSocket; 
		
			setupStreams();
		}
	
		
		private void setupStreams() {
			
			try {
				
				iStream = new JsonReader( new InputStreamReader (clSocket.getInputStream()) );
				oStream = new JsonWriter( new OutputStreamWriter(clSocket.getOutputStream()) );
				
			}catch (Exception ex) {
				
			}
			
		}
		
		public void run() {
				
				
			while(playing) {
								
				try {
					
					if(iStream.hasNext()) {
						MockGameObject test = parser.fromJson(iStream, MockGameObject.class);
						SendToAll(test);
					}
					
					/*
					String line;
					while( (line = iStream.readLine()) != null) {
						msg.append(line);
											
					}
					messages.add( msg.toString() );*/
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
		
		public void Send(MockGameObject msg) {
			
			//oStream.write(msg);
			parser.toJson(msg, MockGameObject.class, oStream);
		}
	
	}
	
	public void SendToAll(MockGameObject info) {
		
		for(ClientThread ct : connectedClients) {
			ct.Send(info);
		}
	}
}
