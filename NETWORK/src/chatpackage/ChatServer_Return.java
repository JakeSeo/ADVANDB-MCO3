package chatpackage;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatServer_Return implements Runnable {

	Socket SOCKET;
	private Scanner INPUT;
	private PrintWriter OUT;
	String MESSAGE = "";
	
	public ChatServer_Return(Socket S)
	{
		this.SOCKET = S;
	}
	
	public void CheckConnection() throws IOException
	{
		if(!SOCKET.isConnected())
		{
			for(int i = 1; i <= ChatServer.Conn.size(); i++)
			{
				if(ChatServer.Conn.get(i) == SOCKET)
				{
					ChatServer.Conn.remove(i);
				}
			}
			
			for(int i = 1; i <= ChatServer.Conn.size(); i++)
			{
				Socket TempSocket = (Socket) ChatServer.Conn.get(i-1);
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
					
					for(int i = 1; i <= ChatServer.Conn.size(); i++)
					{
						Socket TempSocket = (Socket) ChatServer.Conn.get(i-1);
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

