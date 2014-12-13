package netsubmodul;

import logicmodule.Opcode;
import logicmodule.OpcodeHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author bs
 */
public class Client extends NetComponent implements IClient {

	
	private Socket connection;
	private Thread listenerThread;
	private ObjectInputStream iStream;
	private ObjectOutputStream oStream;
	private boolean clientInGame;
	private int clientID = -100;


	public boolean startClient(String ip, int port) {
		
		
		try {

			connection = new Socket("127.0.0.1", port);
			oStream = new ObjectOutputStream(connection.getOutputStream());
			iStream = new ObjectInputStream(connection.getInputStream());

			clientInGame = true;
			listenerThread = SpawnListenerThread();
			
			listenerThread.setDaemon(true);
			listenerThread.start();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if(listenerThread == null)
			return false;
		
		return listenerThread.isAlive() && connection.isConnected();
		
		
	}

	public void stopClient() {

		clientInGame = false;
		try {
			iStream.close();
			oStream.close();
			connection.close();
			listenerThread.join();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

	public static void main(String args[]) {

		new Client().startClient("127.0.0.1", 9999);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void setMessageHandler(OpcodeHandler handler) {

		this.msgHandler = handler;
	}

	
	private Thread SpawnListenerThread() {
		
		return new Thread(new Runnable() {

			public void run() {

				while(clientID == -100) {

					Integer idFromServer = null;

					try {
						idFromServer = (Integer)iStream.readObject();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}


					System.out.println(idFromServer);
					clientID = idFromServer;
				}


				while(clientInGame) {

					//TODO : do something
				}
			}
			
			
		});
				
		
	}


	@Override
	public boolean Send(Opcode obj) {

		//TODO: set sender id in Opcode

		try {
			oStream.writeObject(obj);
		} catch (IOException e) {

			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean SendToOne(Opcode obj, int clientId) {



		obj.setRecipientID(clientId);
		Send(obj);

		return true;
	}


	private void ParseMessage(Opcode msg) {

		if(this.msgHandler != null) {

			msgHandler.decodeOpcode(msg);
		}

	}


	public int GetClientID() {
		return clientID;
	}

	public void setClientID(int id) {

		clientID = id;
	}

}
