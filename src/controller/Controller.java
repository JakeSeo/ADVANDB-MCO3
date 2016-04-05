package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.rowset.CachedRowSetImpl;
import java.util.concurrent.CyclicBarrier;

import model.DBModel;
import model.Node;
import model.TableContents;
import model.Transaction;
import model.TransactionThread;
import view.MainGUI;

public class Controller {
	String type;
	String ip;
	Node central;
	Node marin;
	Node palawan;
	final static int Port = 1234;
	ArrayList<Transaction> transactions;
	DBModel dbm;
	MainGUI view;
        CyclicBarrier cb;

	public Controller(String ip, String type) {
		transactions = new ArrayList<Transaction>();
		this.type = type;
		this.ip = ip;
		palawan = new Node();
		marin = new Node();
		central = new Node();
		dbm = new DBModel();
                cb = new CyclicBarrier(1);
	}

	public Node getCentral() {
		return central;
	}

	public void setView(MainGUI mainGUI) {
		this.view = mainGUI;
	}

	public Node getMarin() {
		return marin;
	}

	public Node getPalawan() {
		return palawan;
	}

	public String getType() {
		return type;
	}
        
        public void setCyclicBarrierSize(int size)
        {
		cb = new CyclicBarrier(size);
        }

	public void add(String ip, String name) {
		if (name.equals("Marinduque")) {
			marin.setIpadd(ip);
			marin.setName(name);
		} else if (name.equals("Palawan")) {
			palawan.setIpadd(ip);
			palawan.setName(name);
		} else if (name.equals("Central")) {
			central.setIpadd(ip);
			central.setName(name);
		}
	}
	
	public void executeQuery(Transaction t, String ip)
	{
		if(this.ip.equals(ip)) {
			// local trans.
			
		} else {
			TransactionThread tt = new TransactionThread(t, this, ip, cb);
			tt.run();
		}
	}

	public void sendTransaction(String name, String query, int isolationLvl, int end) {
                Transaction t = new Transaction(name, query, isolationLvl, end);
		if (type.equals("Central")) 
                {
                    System.out.println(t.toString()); // query locally here.
                    TransactionThread tt = new TransactionThread(t, this, ip, cb);
                    new Thread(tt).start();
                    
                        
		} else {
			Socket SOCK;
			String ip = central.getIpadd();
			try {
				SOCK = new Socket(ip, Port); // Open socket
				String first = "\"TRANSACTION\" ";
				byte[] prefix = first.getBytes();
				byte[] mybytearray = serialize(t);
				byte[] finalByte = byteConcat(prefix, mybytearray);
				InputStream is = new ByteArrayInputStream(finalByte);
				int bytesRead = is.read(mybytearray, 0, mybytearray.length);
				OutputStream os = SOCK.getOutputStream(); // Get socket output
															// stream to send
															// file through //
				os.write(finalByte, 0, finalByte.length); // Send file bytes
															// through socket
				os.flush();
				SOCK.close();
			} catch (UnknownHostException e) {
				if (type.equals("Palawan")) {
					ip = marin.getIpadd();
				} else {
					ip = palawan.getIpadd();
				}
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void receiveTransaction(String senderIp, byte[] bytes, String receiverIp) {
		// Receive Transaction then send the result(TableContents) back to the sender
		System.out.println("Receive Transaction (start)");
		
		try{
			byte[] mybytearray = new byte[65500];							// Creates a byte array
			InputStream is = new ByteArrayInputStream(bytes);				// Creates an input stream from the given bytes
			int bytesRead = is.read(mybytearray, 0, mybytearray.length);	// Reads bytes from bytes to mybytearray and stores number of bytes read
			
			
			System.out.println("writeFile (bytes received: " + bytesRead + ")");
		
			Transaction t = (Transaction) deserialize(mybytearray);
			transactions.add(t);

			System.out.println("Received from " + senderIp);
			System.out.println("Transaction Name " + transactions.get(transactions.size()-1).getName());
			System.out.println("Transaction Iso Level " + transactions.get(transactions.size()-1).getIsolationLvl());
			System.out.println("Transaction End " + transactions.get(transactions.size()-1).getEnd());
			System.out.println("Transaction Query " + transactions.get(transactions.size()-1).getQuery());
			
			System.out.println("");
			
			executeQuery(t, senderIp);
			
			/*TransactionThread tThread = new TransactionThread(t, this, senderIp); 
			tThread.run();*/
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Receive Transaction (end)");
		
	}
	
	public void sendTableContents(TableContents tc, String ip) {
		// Send the tablecontents of the transactionName to the ip
		System.out.println("sendTC (start)");
		if(!ip.equals(this.ip))
		{
			try {
				  Socket SOCK;						// Open socket
				  SOCK = new Socket(ip, 1234);
				
	
	          	  String first="\"SENDTABLECONTENTS\" ";
	           	  byte[] prefix = first.getBytes();
	           	  byte[] mybytearray = serialize(tc);
	              byte[] finalByte = byteConcat(prefix, mybytearray);
				  InputStream is = new ByteArrayInputStream(finalByte);				// Creates an input stream from the given bytes
				  //FileOutputStream fos = new FileOutputStream(filename);			// Creates an output stream for writing to the file
				  //BufferedOutputStream bos = new BufferedOutputStream(bytes);
				  int bytesRead = is.read(mybytearray, 0, mybytearray.length);
				  OutputStream os = SOCK.getOutputStream();											// Get socket output stream to send file through											// 
				  os.write(finalByte, 0, finalByte.length);										// Send file bytes through socket
				  os.flush();				
				  SOCK.close();																		// Close socket
				  
				  System.out.println("sendTC (bytes sent to " + ip + ": " + bytesRead + ")");
			  } catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			  } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			  }
		}else
		{
			updateTable(tc);
		}
		
	}
	
	public void receiveTableContents(String senderIp, byte[] bytes) {
		// Receive tablecontents then update the table

		  System.out.println("Receive TC (start)");
			try{
				byte[] mybytearray = new byte[65500];							// Creates a byte array
				InputStream is = new ByteArrayInputStream(bytes);				// Creates an input stream from the given bytes
				int bytesRead = is.read(mybytearray, 0, mybytearray.length);	// Reads bytes from bytes to mybytearray and stores number of bytes read
				
				
				System.out.println("Receive TC (bytes received: " + bytesRead + ")");
			
				TableContents tc = (TableContents) deserialize(mybytearray);

				System.out.println("Table Contents transaction names: " + tc.getTransactionName());
				
				/*for(int x = 0; x < tc.getColumnNames().length; x++)
				{
					System.out.println("Column " + x + ": " + tc.getColumnNames()[x]);
				}*/
				
				int y = 0;
				CachedRowSetImpl cas = tc.getData();
				while(cas.next())
				{
					System.out.println("Data id " + (y+1) + ": " + cas.getInt(1));
					System.out.println("Data num 1 " + (y+1) + ": " + cas.getInt(2));
					System.out.println("Data num 2 " + (y+1) + ": " + cas.getInt(3));
					y++;
				}
				updateTable(tc);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		  
		 // addFileToChat(ip, filename);	// Display file in chat
		  
		  System.out.println("Receive TC (end)");
	}
	
	public void updateTable(TableContents tc) {
		// view.updateTable(tc);
		view.populateTable(tc.getTransactionName(), tc.getColumnNames(), getTableRows(tc));
	}
	

	public Object[][] getTableRows(TableContents tc) {
		CachedRowSetImpl cas = tc.getData();
		ArrayList<Object[]> tempRowData = new ArrayList<>(0);

		try {
			while (cas.next()) {
				Object[] rowdata = new Object[tc.getColumnNames().length];

				for (int i = 0; i < tc.getColumnNames().length; i++) {
                                    rowdata[i] = cas.getObject(i + 1);
				}
				tempRowData.add(rowdata);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertArrayListToObjectArray(tempRowData);
	}

	public Object[][] convertArrayListToObjectArray(ArrayList<Object[]> tempRows) {
		Object[][] newRows = new Object[tempRows.size()][];

		for (int i = 0; i < tempRows.size(); i++) {
			newRows[i] = tempRows.get(i);
		}

		return newRows;
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}

	public static byte[] byteConcat(byte[] A, byte[] B) {
		int aLen = A.length;
		int bLen = B.length;
		byte[] C = new byte[aLen + bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}

	// Returns string cut off at either space, null or eof, depending on c
	public String getUntilSpaceOrNull(String bytesinstring, char c) {
		System.out.println("getUntilSpaceOrNull");

		int i = 0; // Character index
		char[] bytesinchar = bytesinstring.toCharArray(); // String converted to
															// char array

		// Space or null
		if (c == 'b')
			while (bytesinchar[i] != ' ' && bytesinchar[i] != '\0')
				i++;

		// Null
		else if (c == 'n')
			while (bytesinchar[i] != '\0')
				i++;

		// Space
		else if (c == 's')
			while (bytesinchar[i] != ' ')
				i++;

		// Null or EOF
		else if (c == 'e')
			while (bytesinchar[i] != '\0' && bytesinchar[i] != '\u001a')
				i++;

		// Return cut up string
		return bytesinstring.substring(0, i);
	}

}
