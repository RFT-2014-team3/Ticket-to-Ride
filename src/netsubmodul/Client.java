package netsubmodul;

import logicmodule.Opcode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author bs
 */
public class Client implements IClient {

	
	private Socket connection;
	private Thread listenerThread;
	
	
	public boolean startClient(String ip, int port) {
		
		
		try {
			InetAddress address = InetAddress.getByName(ip);
			connection = new Socket(address, port);
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
		finally {
			try {
				connection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(listenerThread == null)
			return false;
		
		return listenerThread.isAlive();
		
		
	}



	public static void main(String args[]) {
		
		boolean connect = new Client().startClient("127.0.0.1", 9999);
		
		if(connect) {
			System.out.println("Connection successful");
		}

		while(true){}
	}
	
	
	
	private Thread SpawnListenerThread() {
		
		return new Thread(new Runnable() {

			public void run() {
								
			}
			
			
		});
				
		
	}


	@Override
	public boolean SendToAll(Opcode obj) {
		throw new NotImplementedException();
	}

	@Override
	public boolean SendToOne(Opcode obj, int clientId) {
		throw new NotImplementedException();
	}



}
