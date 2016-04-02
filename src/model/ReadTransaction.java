package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ReadTransaction implements Transaction, Runnable{

	private int timeStamp;
	Connection conn;
	DBConnection db;
	Statement stmt;
	private int iso;
	BufferedReader br;
	BufferedWriter bw;
	CyclicBarrier cb;
	private int id, number, value, ending;
	
	public ReadTransaction(CyclicBarrier cb, int isolation_level, int id, int number, int value, int ending)
	{
		this.cb = cb;
		
		this.id = id;
		this.number = number;
		this.value = value;
		this.ending = ending;
		
		db = new DBConnection();
		conn = db.getConnection();
		this.iso = isolation_level;
		/*try {
			bw = new BufferedWriter(new FileWriter("log.txt", true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	@Override
	public int getTimeStamp() {
		// TODO Auto-generated method stub
		return timeStamp;
	}

	@Override
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
		
	}

	@Override
	public void setIsolationLevel(int iso_level) {
		System.out.println("Setting isolation level for read transaction as: " + iso_level);
		
		iso = iso_level;
		
		try {
			
			switch(iso) {
				case READ_UNCOMMITTED:
						conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					break;
				case READ_COMMITTED:
						conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
					break;
				case REPEATABLE_READS:
						conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				 	break;
				case SERIALIZABLE:
						conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					break;
				default:
						conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public int getIsolationLevel() {
		return iso;
	}

	@Override
	public void beginTransaction() {
		System.out.println("Reading Transaction begins");
		
		
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("START TRANSACTION;");
			ps.executeQuery();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void executeTransaction(int id, int number, int value)
	{		
		System.out.println("Running the Query for the Transaction: ");
		try {
			String lock = "LOCK table tbl READ";
			PreparedStatement ls = conn.prepareStatement(lock);
			ls.execute();
			String query = null;
			
			switch(number)
			{
				case 1: query = "SELECT id,num1 FROM tbl WHERE id = ?";
						break;
				case 2: query = "SELECT id,num2 FROM tbl WHERE id = ?";
						break;
				default: query = "SELECT * FROM tbl";
						break;
			}
			
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, value);
			ps.setInt(2, id);
			ps.executeUpdate();
			
			String unlock = "UNLOCK tables;";
			PreparedStatement us = conn.prepareStatement(unlock);
			us.execute();
			
			ls.close();
			ps.close();
			us.close();
			System.out.println("Done Reading!");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void endTransaction(int ending) {
		// TODO Auto-generated method stub
		System.out.println("Reading has ended.");
		
		try {
			
			switch(ending) {
				case COMMIT: conn.commit();
					break;
				case ROLLBACK: conn.rollback();
					break;
				default: conn.rollback();
					break;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			cb.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BrokenBarrierException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Running Reader...");
		beginTransaction();
		executeTransaction(id, number, value);
		endTransaction(ending);
	}
	

}
