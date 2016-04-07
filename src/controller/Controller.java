package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.rowset.CachedRowSetImpl;
import java.net.InetSocketAddress;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import model.Node;
import model.SerializableTransaction;
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
	MainGUI view;
	CyclicBarrier cb;
	TableContents localdata;

	public String getIp() {
		return ip;
	}

	public Controller(String ip, String type) {
		transactions = new ArrayList<Transaction>();
		this.type = type;
		this.ip = ip;
		palawan = new Node();
		marin = new Node();
		central = new Node();
		cb = new CyclicBarrier(1);
		localdata = null;
	}
        
        public boolean isConnected(String ip)
        {
            try {
                Socket sock = new Socket();
                sock.connect(new InetSocketAddress(ip, Port), 10000);
                sock.close();
            } catch (IOException ex) {
                System.out.println("Failed to Connect to a node");
                ex.printStackTrace();
                return false;
            }
            return true;
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

	public void setCyclicBarrierSize(int size) {
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

	public void executeQuery(Transaction t, String ip) {
		TransactionThread tt = new TransactionThread(t, this, ip, cb);
		new Thread(tt).start();
	}

	public void okCommit(Transaction t) {
		Socket SOCK;
		SerializableTransaction sertrans = new SerializableTransaction(t.getName(),t.getQuery(), t.getISObyInt(t.getIsolationLvl()), t.getEndByInt(t.getEnd()), t.getDatabase(), t.getTransType());
		try {
            if(isConnected(central.getIpadd()))
            {
                SOCK = new Socket(central.getIpadd(), Port); // Open socket
                String first = "\"OKCOMMIT\" ";
				byte[] prefix = first.getBytes();
				byte[] mybytearray = serialize(sertrans);
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
            }
		} catch (UnknownHostException e) {
			System.out.println("Sorry ka wala kang makukuha.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*public void commit() {
		dbm.commit();
        dbm.unlockTable();
	}*/

	public void commit(String senderIp, byte[] bytes) {
		System.out.println("Receive Commit (start)");

		try {
			byte[] mybytearray = new byte[655000]; // Creates a byte array
			InputStream is = new ByteArrayInputStream(bytes); // Creates an
																// input stream
																// from the
																// given bytes
			int bytesRead = is.read(mybytearray, 0, mybytearray.length); // Reads
																			// bytes
																			// from
																			// bytes
																			// to
																			// mybytearray
																			// and
																			// stores
																			// number
																			// of
																			// bytes
																			// read

			System.out.println("writeFile (bytes received: " + bytesRead + ")");

			SerializableTransaction st = (SerializableTransaction) deserialize(mybytearray);
			Transaction t = new Transaction(st.getName(), st.getQuery(), st.getIsolationLvl(), st.getEnd(), st.getDatabase(), st.getTransType());

			System.out.println("Received from " + senderIp);
			System.out.println("Transaction Name " + transactions.get(transactions.size() - 1).getName());
			System.out.println("Transaction Iso Level " + transactions.get(transactions.size() - 1).getIsolationLvl());
			System.out.println("Transaction End " + transactions.get(transactions.size() - 1).getEnd());
			System.out.println("Transaction Query " + transactions.get(transactions.size() - 1).getQuery());
			System.out.println("Transaction Database " + transactions.get(transactions.size() - 1).getDatabase());
			System.out.println("Transaction Type " + transactions.get(transactions.size() - 1).getTransType());

			
			transactions.get(getTransactionWithName(st.getName())).setAC();

			if(t.getEndByInt((transactions.get(getTransactionWithName(st.getName())).getEnd())).equalsIgnoreCase("Commit"))
			transactions.get(getTransactionWithName(st.getName())).commit();
			else transactions.get(getTransactionWithName(st.getName())).rollback();
			
			transactions.remove(getTransactionWithName(st.getName()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Receive Transaction (end)");
	}

	public void queryLocally(Transaction t) {
		localdata = null;
		localdata = t.getData(t);
	}
	
	public int getTransactionWithName(String name) {
		for (int i = 0; i < transactions.size(); i++) {
			if(transactions.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public void okWrite(Transaction t) {
		Socket SOCK;
		String ip = null;
		SerializableTransaction sertrans = new SerializableTransaction(t.getName(), t.getQuery(), t.getISObyInt(t.getIsolationLvl()), t.getEndByInt(t.getEnd()), t.getDatabase(), t.getTransType());
		if(t.getDatabase().equals("Palawan") || t.getDatabase().equals("Marinduque"))
		{
			if (t.getDatabase().equals("Palawan")) {
				ip = palawan.getIpadd();
			} else if (t.getDatabase().equals("Marinduque")) {
				ip = marin.getIpadd();
			}
                        
                        if(isConnected(ip))
                        {
                            try {
				SOCK = new Socket(ip, Port); // Open socket
				String first = "\"OKWRITE\" ";
				byte[] prefix = first.getBytes();
				byte[] mybytearray = serialize(sertrans);
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
				System.out.println("Sorry ka wala kang makukuha.");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                        }
			
		} else 
		{   
                        if(isConnected(palawan.getIpadd()))
                        {
                            try {
				SOCK = new Socket(palawan.getIpadd(), Port); // Open socket
				String first = "\"OKWRITE\" ";
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
				System.out.println("Sorry ka wala kang makukuha.");
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
                        }
			
			if(isConnected(marin.getIpadd()))
                        {
                            try {
				SOCK = new Socket(marin.getIpadd(), Port); // Open socket
				String first = "\"OKWRITE\" ";
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
                                    System.out.println("Sorry ka wala kang makukuha.");
                                    //e.printStackTrace();
                            } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    //e.printStackTrace();
                            }
                        }
			
		}
	}

	// Sending a read transaction request
	public void sendTransaction(String name, String query, String database, String isolationLvl, String end) {

		//Transaction t = new Transaction(name, query, isolationLvl, end, database, "G");
		SerializableTransaction t = new SerializableTransaction(name, query, isolationLvl, end, database, "G");
		
		//t.setNodeType(type);
		/*
		 * if(t.getQuery().startsWith("SELECT")) {
		 */
		// If any Local Transaction

		if (query.toLowerCase().startsWith("update")) {
			
			if(type.equals("Central")) 
            {
                t.setTransType("U");
                Transaction transaction = new Transaction(t.getName(), t.getQuery(), t.getIsolationLvl(), t.getEnd(), t.getDatabase(), t.getTransType());
                transactions.add(transaction);
                System.out.println("SEND UPDATE REQUEST");
                TransactionThread tt = new TransactionThread(transaction, this, ip, cb);
				new Thread(tt).start();  
            }
			else 
			{
				Socket SOCK;
                t.setTransType("U");
                //Transaction transaction = new Transaction(t.getName(), t.getQuery(), t.getIsolationLvl(), t.getEnd(), t.getDatabase(), t.getTransType());
                if(isConnected(central.getIpadd()))
                {
                    try {
					SOCK = new Socket(central.getIpadd(), Port); // Open socket
					String first = "\"TRANSACTION\" ";
					byte[] prefix = first.getBytes();
					byte[] mybytearray = serialize(t);
					byte[] finalByte = byteConcat(prefix, mybytearray);
					InputStream is = new ByteArrayInputStream(finalByte);
					int bytesRead = is.read(mybytearray, 0, mybytearray.length);
					OutputStream os = SOCK.getOutputStream(); // Get socket
					os.write(finalByte, 0, finalByte.length); // Send file bytes
					os.flush();
					SOCK.close();
				} catch (UnknownHostException e) {
					System.out.println("Sorry ka wala kang makukuha.");
					e.printStackTrace();
				} catch(SocketException ex)
	                {
	                    //ex.printStackTrace();
	                    System.out.println("CENTRAL IS DOWN");
	                    
	                } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
             }                      
		  }

		} else {
			if (type.equals("Central") && database.equals("Central")
				|| type.equals("Marinduque") && database.equals("Marinduque")
				|| type.equals("Palawan") && database.equals("Palawan"))
			{
				t.setTransType("L");
				System.out.println(t.toString()); // query locally here.
                Transaction transaction = new Transaction(t.getName(), t.getQuery(), t.getIsolationLvl(), t.getEnd(), t.getDatabase(), t.getTransType());
				TransactionThread tt = new TransactionThread(transaction, this, ip, cb);
				new Thread(tt).start();
			}
			// If any single site global transaction
			else if (type.equals("Central") && (database.equals("Palawan") || database.equals("Marinduque"))
					|| type.equals("Palawan") && database.equals("Marinduque")
					|| type.equals("Marinduque") && database.equals("Palawan")) {
				t.setTransType("S");
				Socket SOCK;
				String ip = null;
				if (database.equals("Palawan"))
					ip = palawan.getIpadd();
				else
					ip = marin.getIpadd();

                if(isConnected(ip))
                {
                    //Transaction transaction = new Transaction(t.getName(), t.getQuery(), t.getIsolationLvl(), t.getEnd(), t.getDatabase(), t.getTransType());
                    try {
					SOCK = new Socket(ip, Port); // Open socket
					String first = "\"TRANSACTION\" ";
					byte[] prefix = first.getBytes();
					byte[] mybytearray = serialize(t);
					byte[] finalByte = byteConcat(prefix, mybytearray);
					InputStream is = new ByteArrayInputStream(finalByte);
					int bytesRead = is.read(mybytearray, 0, mybytearray.length);
					OutputStream os = SOCK.getOutputStream(); // Get socket
																// output
																// stream to
																// send
																// file through
																// //
					os.write(finalByte, 0, finalByte.length); // Send file bytes
																// through
																// socket
					os.flush();
					SOCK.close();
				} catch (UnknownHostException e) {
					System.out.println("Sorry ka wala kang makukuha.");
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }
				
			}
			// if you're at any branch and you want to get all data
			else if ((type.equals("Marinduque") || type.equals("Palawan")) && database.equals("Central")) {
				t.setTransType("G");
				Socket SOCK;
				System.out.println("Central's IP " + central.getIpadd());
				String ip = central.getIpadd();
                //Transaction transaction = new Transaction(t.getName(), t.getQuery(), t.getIsolationLvl(), t.getEnd(), t.getDatabase(), t.getTransType());
                if(isConnected(ip))
                {
                   try {
					SOCK = new Socket(ip, Port); // Open socket
					String first = "\"TRANSACTION\" ";
					byte[] prefix = first.getBytes();
					byte[] mybytearray = serialize(t);
					byte[] finalByte = byteConcat(prefix, mybytearray);
					InputStream is = new ByteArrayInputStream(finalByte);
					int bytesRead = is.read(mybytearray, 0, mybytearray.length);
					OutputStream os = SOCK.getOutputStream(); // Get socket
																// output
																// stream to
																// send
																// file through
																// //
					os.write(finalByte, 0, finalByte.length); // Send file bytes
																// through
																// socket
					os.flush();
					SOCK.close();
                    } catch (SocketException e)
                   {
                    t.setTransType("R");
                    if (type.equals("Palawan")) {
                            ip = marin.getIpadd();
                    } else {
                            ip = palawan.getIpadd();
                    }
                    Transaction transaction = new Transaction(t.getName(), t.getQuery(), t.getIsolationLvl(), t.getEnd(), t.getDatabase(), t.getTransType());
					queryLocally(transaction);
					// do the get from the other side here and merge
                    if(isConnected(ip))
                    {
					try {
						SOCK = new Socket(ip, Port); // Open socket
						String first = "\"TRANSACTION\" ";
						byte[] prefix = first.getBytes();
						byte[] mybytearray = serialize(t);
						byte[] finalByte = byteConcat(prefix, mybytearray);
						InputStream is = new ByteArrayInputStream(finalByte);
						int bytesRead = is.read(mybytearray, 0, mybytearray.length);
						OutputStream os = SOCK.getOutputStream(); // Get socket
																	// output
																	// stream to
																	// send
																	// file
																	// through
																	// //
						os.write(finalByte, 0, finalByte.length); // Send file
																	// bytes
																	// through
																	// socket
						os.flush();
						SOCK.close();
					} catch (UnknownHostException ex) {
						ex.printStackTrace();
					} catch (IOException ex) {
						System.out.println("CENTRAL IS DOWN");
					}
                  }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }
				
			}
		}
		/*
		 * } else { t.setTransType("W"); Socket SOCK; String ip =
		 * central.getIpadd();
		 * 
		 * try { SOCK = new Socket(ip, Port); // Open socket String first =
		 * "\"TRANSACTION\" "; byte[] prefix = first.getBytes(); byte[]
		 * mybytearray = serialize(t); byte[] finalByte = byteConcat(prefix,
		 * mybytearray); InputStream is = new ByteArrayInputStream(finalByte);
		 * int bytesRead = is.read(mybytearray, 0, mybytearray.length);
		 * OutputStream os = SOCK.getOutputStream(); // Get socket output //
		 * stream to send // file through // os.write(finalByte, 0,
		 * finalByte.length); // Send file bytes // through socket os.flush();
		 * SOCK.close(); } catch (SocketException e) { t.setTransType("R"); if
		 * (type.equals("Palawan")) { ip = marin.getIpadd(); } else { ip =
		 * palawan.getIpadd(); }
		 * 
		 * queryLocally(t); // do the get from the other side here and merge
		 * 
		 * try { SOCK = new Socket(ip, Port); // Open socket String first =
		 * "\"TRANSACTION\" "; byte[] prefix = first.getBytes(); byte[]
		 * mybytearray = serialize(t); byte[] finalByte = byteConcat(prefix,
		 * mybytearray); InputStream is = new ByteArrayInputStream(finalByte);
		 * int bytesRead = is.read(mybytearray, 0, mybytearray.length);
		 * OutputStream os = SOCK.getOutputStream(); // Get socket output //
		 * stream to send // file through // os.write(finalByte, 0,
		 * finalByte.length); // Send file bytes // through socket os.flush();
		 * SOCK.close(); } catch (UnknownHostException ex) {
		 * ex.printStackTrace(); } catch (IOException ex) {
		 * ex.printStackTrace(); }finally {
		 * 
		 * } } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */
	}

	public void receiveTransaction(String senderIp, byte[] bytes) {
		// Receive Transaction then send the result(TableContents) back to the
		// sender
		System.out.println("Receive Transaction (start)");

		try {
			byte[] mybytearray = new byte[655000]; // Creates a byte array
			InputStream is = new ByteArrayInputStream(bytes); // Creates an
																// input stream
																// from the
																// given bytes
			int bytesRead = is.read(mybytearray, 0, mybytearray.length); // Reads
																			// bytes
																			// from
																			// bytes
																			// to
																			// mybytearray
																			// and
																			// stores
																			// number
																			// of
																			// bytes
																			// read

			System.out.println("writeFile (bytes received: " + bytesRead + ")");

			SerializableTransaction t = (SerializableTransaction) deserialize(mybytearray);
			Transaction trans = new Transaction(t.getName(), t.getQuery(), t.getIsolationLvl(), t.getEnd(), t.getDatabase(), t.getTransType());
			transactions.add(trans);

			System.out.println("Received from " + senderIp);
			System.out.println("Transaction Name " + transactions.get(transactions.size() - 1).getName());
			System.out.println("Transaction Iso Level " + transactions.get(transactions.size() - 1).getIsolationLvl());
			System.out.println("Transaction End " + transactions.get(transactions.size() - 1).getEnd());
			System.out.println("Transaction Query " + transactions.get(transactions.size() - 1).getQuery());
			System.out.println("Transaction Database " + transactions.get(transactions.size() - 1).getDatabase());
			System.out.println("Transaction Type " + transactions.get(transactions.size() - 1).getTransType());

			System.out.println("");
            executeQuery(trans, senderIp);
                        

			/*
			 * TransactionThread tThread = new TransactionThread(t, this,
			 * senderIp); tThread.run();
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Receive Transaction (end)");

	}

	public void sendTableContents(TableContents tc, String transactiontype, String ip) {
		// Send the tablecontents of the transactionName to the ip
		System.out.println("sendTC (start)");
		if (!ip.equals(this.ip)) {
			try {
				Socket SOCK; // Open socket
                if(isConnected(ip))
                {
                    SOCK = new Socket(ip, 1234);
                    tc.setTransType(transactiontype);

				transactiontype = transactiontype + " ";
				String first = "\"SENDTABLECONTENTS\" ";
				byte[] prefix = first.getBytes();
				byte[] mybytearray = serialize(tc);
				byte[] finalByte = byteConcat(prefix, mybytearray);
				// byte[] semifinalByte = byteConcat(prefix, mybytearray);
				// byte[] finalByte = byteConcat(semifinalByte, type);
				InputStream is = new ByteArrayInputStream(finalByte); // Creates
																		// an
																		// input
																		// stream
																		// from
																		// the
																		// given
																		// bytes
				// FileOutputStream fos = new FileOutputStream(filename); //
				// Creates an output stream for writing to the file
				// BufferedOutputStream bos = new BufferedOutputStream(bytes);
				int bytesRead = is.read(mybytearray, 0, mybytearray.length);
				OutputStream os = SOCK.getOutputStream(); // Get socket output
															// stream to send
															// file through //
				os.write(finalByte, 0, finalByte.length); // Send file bytes
															// through socket
				os.flush();
				SOCK.close(); // Close socket

				System.out.println("sendTC (bytes sent to " + ip + ": " + bytesRead + ")");
                                }
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Pumasok sa else");
			updateTable(tc, "L");
		}

	}

	public void receiveTableContents(String senderIp, byte[] bytes) {
		// Receive tablecontents then update the table
		System.out.println("Receive TC (start)");
		try {
			byte[] mybytearray = new byte[655000]; // Creates a byte array
			InputStream is = new ByteArrayInputStream(bytes); // Creates an
																// input stream
																// from the
																// given bytes
			int bytesRead = is.read(mybytearray, 0, mybytearray.length); // Reads
																			// bytes
																			// from
																			// bytes
																			// to
																			// mybytearray
																			// and
																			// stores
																			// number
																			// of
																			// bytes
																			// read

			System.out.println("Receive TC (bytes received: " + bytesRead + ")");
                        TableContents tc = (TableContents) deserialize(mybytearray);
			System.out.println("Table Contents transaction name: " + tc.getTransactionName());
                        String transtype = tc.getTransType();
			/*
			 * for(int x = 0; x < tc.getColumnNames().length; x++) {
			 * System.out.println("Column " + x + ": " +
			 * tc.getColumnNames()[x]); }
			 */

			int y = 0;
			CachedRowSetImpl cas = tc.getData();
			while (cas.next()) {
				System.out.println("Data id " + (y + 1) + ": " + cas.getInt(1));
				System.out.println("Data num 1 " + (y + 1) + ": " + cas.getInt(2));
				System.out.println("Data num 2 " + (y + 1) + ": " + cas.getInt(3));
				y++;
			}

			updateTable(tc, transtype);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Receive TC (end)");
	}

	public void updateTable(TableContents tc, String trans) {
		// view.updateTable(tc);
		if (!trans.equals("R"))
			view.populateTable(tc.getTransactionName(), tc.getColumnNames(), getTableRows(tc));
		else
			view.populateTable(tc.getTransactionName(), tc.getColumnNames(), getTableRows(tc, localdata));
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

	public Object[][] getTableRows(TableContents tc, TableContents loc) {
		CachedRowSetImpl cas = tc.getData();
		CachedRowSetImpl cas2 = loc.getData();
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

		try {
			while (cas2.next()) {
				Object[] rowdata = new Object[tc.getColumnNames().length];

				for (int i = 0; i < tc.getColumnNames().length; i++) {
					rowdata[i] = cas2.getObject(i + 1);
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
                byte[] temp = out.toByteArray();
                out.close();
                os.close();
		return temp;
	}

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
                Object temp = is.readObject();
                is.close();
                in.close();
		return temp;
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

	/*
	 * public String getIPAdd(String name) { if(name.equals("Central")) return
	 * central.getIpadd(); else if(name.equals("Marinduque")) return
	 * marin.getIpadd(); else if(name.equals("Palawan")) return
	 * palawan.getIpadd(); else return central.getIpadd(); }
	 * 
	 * public boolean isAlive(String ipAdd) { System.out.println(
	 * "Gonna check if socket is alive"); Socket SOCK = null; try{ SOCK = new
	 * Socket(ipAdd, Port); PrintWriter OUT = new
	 * PrintWriter(SOCK.getOutputStream()); OUT.println(
	 * "checking if it is alive"); OUT.flush(); SOCK.close(); return true;
	 * }catch(Exception e) { System.out.println("It is not alive " + e); return
	 * false; }
	 * 
	 * }
	 */

}
