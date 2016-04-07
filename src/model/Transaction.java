package model;

import java.io.Serializable;
import java.sql.SQLException;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Transaction implements Serializable{
	
	public static int ISO_READ_UNCOMMITTED = 1;
	public static int ISO_READ_COMMITTED = 2;
	public static int ISO_REPEATABLE_READ = 3;
	public static int ISO_SERIALIZABLE = 4;
	
	public static int COMMIT = 5;
	public static int ROLLBACK = 6;
	
	private String name;
	private String query;
	private String writeQuery;
	private int isolationLvl;
	private int end;
	private String database;
	private String transtype;
	private Connection conn;
	private DBConnection db;
	
	public Transaction(String name, String query, String isolationLvl, String end, String database, String transtype) {
		this.name = name;
		//this.isolationLvl = isolationLvl;
		//this.end = end;
		setIsolationLevel(isolationLvl);
		setEnding(end);
		this.query = query;
		this.database = database;
		this.transtype = transtype;
		this.writeQuery = query;
		db = new DBConnection();
		conn = db.getConnection();
	}
	
	public String getWriteQuery(){
		return writeQuery;
	}
	
	public String getTransType(){
		return transtype;
	}

	public String getDatabase() {
		return database;
	}
	
	public String getName() {
		return name;
	}

	public String getQuery() {
		return query;
	}

	public int getIsolationLvl() {
		return isolationLvl;
	}

	public int getEnd() {
		return end;
	}
	
	public void setDatabase(String database)
	{
		this.database = database;
	}
	
	public void setTransType(String transtype)
	{
		this.transtype = transtype;
	}
	
	public void setQuery(String query)
	{
		this.query = query;
	}
	
	public void setIsolationLevel(String iso_level) {
		// TODO Auto-generated method stub
		System.out.println("Setting isolation level for transaction 1 to: " + iso_level);
		
			try{
				switch(iso_level) {
				case "Read Uncomitted": isolationLvl = ISO_READ_UNCOMMITTED;
					conn.setTransactionIsolation(ISO_READ_UNCOMMITTED);
					break;
				case "Read Committed": isolationLvl = ISO_READ_COMMITTED;
					conn.setTransactionIsolation(ISO_READ_COMMITTED);
					break;
				case "Repeatable Read": isolationLvl = ISO_REPEATABLE_READ;
					conn.setTransactionIsolation(ISO_REPEATABLE_READ);
				 	break;
				case "Serializable": isolationLvl = ISO_SERIALIZABLE;
					conn.setTransactionIsolation(ISO_SERIALIZABLE);
					break;
				default: isolationLvl = ISO_SERIALIZABLE;
					break;
				}
			}catch(SQLException e)
			{
				e.printStackTrace();
			}
		
	}
	
	public void setEnding(String ending) {
		// TODO Auto-generated method stub
		System.out.println("Setting ending for transaction to: " + ending);
		
			switch(ending) {
			case "Commit": end = COMMIT;
				break;
			case "Rollback": end = ROLLBACK;
				break;
			default: end = COMMIT;
				break;
			}	
	}
	
	public void endTransaction()
	{
		try
		{
			if(end == COMMIT)
				conn.commit();
			else
				conn.rollback();
			
		} catch(SQLException e)
		{
			e.printStackTrace();
		}

	}
	
	public void commit()
	{
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rollback()
	{
		try {
			conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void beginTransaction() {
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("START TRANSACTION;");
			ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void lockTable(String readOrWrite) {
		String q = "LOCK TABLE hpq_crop " + readOrWrite + ";";
		PreparedStatement statement;
		try {
			statement = (PreparedStatement) conn.prepareStatement(q);
			statement.execute(q);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void unlockTable() {
		String q = "UNLOCK TABLES;";
		PreparedStatement statement;
		try {
			statement = (PreparedStatement) conn.prepareStatement(q);
			statement.execute(q);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setAC() {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TableContents getData(Transaction t)
	{
		String q = t.getQuery();
		String transactionName = t.getName();
		ResultSet rs = null;
	    CachedRowSetImpl cs = null;
	    TableContents tc = null;
	    
	        try {
	            //conn = DBConnection.getConnection();
	           // String query = "SELECT * FROM tbl;";
	        	String query = q;
	            PreparedStatement statement = (PreparedStatement) conn.prepareStatement(query);
	            rs = statement.executeQuery();
	            
	            ResultSetMetaData md = rs.getMetaData();
	            int columnCount = md.getColumnCount();
	            
	            String[] columnNames = new String[columnCount];
	            for (int i = 1; i <= columnCount; i++) {
	                columnNames[i - 1] = md.getColumnName(i);
	            }
	            
	            cs = new CachedRowSetImpl();
	            cs.populate(rs);
	            
	            while (true) {
	                if (cs != null)
	                    break;
	            }
	            tc = new TableContents(transactionName, columnNames, cs);
	            System.out.println("DONE EXECUTING TRANS in getdata  " + query);
	            return tc;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }catch(NullPointerException e)
	        {
	            e.printStackTrace();
	        }
	        /*finally {
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }*/
	        return null;
	}
	
	public String updateData(Transaction t)
	{
		String q = t.getQuery();
		String transactionName = t.getName();
		boolean rs = false;
	    CachedRowSetImpl cs = null;
	    TableContents tc = null;
	    
	        try {
	            //conn = DBConnection.getConnection();
	           // String query = "SELECT * FROM tbl;";
	        	String query = q;
	            PreparedStatement statement = (PreparedStatement) conn.prepareStatement(query);
	            rs = statement.execute();

	            System.out.println("DONE EXECUTING TRANS in updatedata  " + query);
	            //System.out.println("update WHERE query is " + q.substring(q.indexOf("WHERE"), q.length()));
	            String newquery = "SELECT * FROM "
	    	    				+ q.substring(q.indexOf("UPDATE")+7, q.indexOf("SET"))
	    	    				+ " WHERE "
	    	    				+ q.substring(q.indexOf("SET")+4, q.indexOf("WHERE")) + ";";
	            System.out.println(newquery);
	            return newquery;
	            //return q.substring(q.indexOf("WHERE"), q.length());
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }catch(NullPointerException e)
	        {
	            e.printStackTrace();
	        }
	        /*finally {
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }*/
	        return null;
	}

	public void updateData(int id, int value) {

		try {
			String query = "UPDATE TABLE tbl SET num1 = ? WHERE id = ?";
			PreparedStatement statement = (PreparedStatement) conn.prepareStatement(query);
			statement.setInt(1, value);
			statement.setInt(2, id);
			statement.execute();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
	
	public String getISObyInt(int iso_level) {
		// TODO Auto-generated method stub
		System.out.println("Setting string isolation level for transaction 1 to: " + iso_level);
		
		if(iso_level == ISO_READ_UNCOMMITTED) return "Read Uncommitted";
		else if(iso_level == ISO_READ_COMMITTED) return "Read Committed";
		else if(iso_level == ISO_REPEATABLE_READ) return "Repeatable Read";
		else if(iso_level == ISO_SERIALIZABLE) return "Serializable";
		else return "Serializable";
		
	}
	
	public String getEndByInt(int end) {
		// TODO Auto-generated method stub
		System.out.println("Setting ending for transaction to: " + end);
		
			if(end == COMMIT) return "Commit";
			else if(end == ROLLBACK) return "Rollback";
			else return "Commit";
	}
	
	
}
