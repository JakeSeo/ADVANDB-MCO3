package network;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerReturn implements Runnable {

	Socket SOCKET;
	private Scanner INPUT;
	private PrintWriter OUT;
	String MESSAGE = "";
	
	public ServerReturn(Socket S)
	{
		this.SOCKET = S;
	}
	
	public void CheckConnection() throws IOException
	{
		if(!SOCKET.isConnected())
		{
			for(int i = 1; i <= DBServer.Conn.size(); i++)
			{
				if(DBServer.Conn.get(i) == SOCKET)
				{
					DBServer.Conn.remove(i);
				}
			}
			
			for(int i = 1; i <= DBServer.Conn.size(); i++)
			{
				Socket TempSocket = (Socket) DBServer.Conn.get(i-1);
				PrintWriter TempOut = new PrintWriter(TempSocket.getOutputStream());
				TempOut.println(TempSocket.getLocalAddress().getHostName() + " disconnected!");
				TempOut.flush();
				System.out.println(TempSocket.getLocalAddress().getHostName() + " disconnected!");
 			}
			
		}
			
	}
		
	
	public void run()
	{
		try
		{
			try
			{
				INPUT = new Scanner(SOCKET.getInputStream());
				OUT = new PrintWriter(SOCKET.getOutputStream());
				
				while(true)
				{
					CheckConnection();
					
					if(!INPUT.hasNext())
					{ return;}
					
					MESSAGE = INPUT.nextLine();
					
					System.out.println("Client said: " + MESSAGE);
					
					for(int i = 1; i <= DBServer.Conn.size(); i++)
					{
						Socket TempSocket = (Socket) DBServer.Conn.get(i-1);
						PrintWriter TempOut = new PrintWriter(TempSocket.getOutputStream());
						TempOut.println(MESSAGE);
						TempOut.flush();
						System.out.println("Sent to: " + TempSocket.getLocalAddress().getHostName());
					
					}
				}
			}
			finally
			{
				SOCKET.close();
			}
		}
		catch(Exception E)
		{
			System.out.println(E);
		}
	}

}

