package chatpackage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {

	public static ArrayList<Socket> Conn = new ArrayList<Socket>();
	public static ArrayList<String> CurrentUsers = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException
	{
		try
		{
			final int PORT = 1050;
			ServerSocket SERVER = new ServerSocket(PORT);
			System.out.println("Waiting for clients...");
			
			while(true)
			{
				Socket SOCKET = SERVER.accept();
				Conn.add(SOCKET);
				
				System.out.println("Client connected from: " + SOCKET.getLocalAddress().getHostName());
				
				AddUserName(SOCKET);
				
				ChatServer_Return CHAT = new ChatServer_Return(SOCKET);
				Thread X = new Thread(CHAT);
				X.start();
				
				
				
			}
			
			
		}
		catch(Exception X)
		{
			System.out.println(X);			
		}
			
	}

	private static void AddUserName(Socket S) throws IOException
	{
		Scanner Input = new Scanner(S.getInputStream());
		String UserName = Input.nextLine();
		CurrentUsers.add(UserName);
		for(int i = 1; i <= ChatServer.Conn.size(); i++)
		{
			Socket TempSocket = (Socket) ChatServer.Conn.get(i-1);
			PrintWriter OUT  = new PrintWriter(TempSocket.getOutputStream());
			OUT.println("#?!" + CurrentUsers);
			OUT.flush();
			
		}
	
	}
	
	
}
