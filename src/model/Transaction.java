package model;

import java.io.Serializable;

public class Transaction implements Serializable{
	private String name;
	private String query;
	private int isolationLvl;
	private int end;
	private String database;
	private String requestingNode;
	private String transtype;
	
	public Transaction(String name, String query, int isolationLvl, int end, String database, String requestingNode, String transtype) {
		this.name = name;
		this.isolationLvl = isolationLvl;
		this.end = end;
		this.query = query;
		this.database = database;
		this.requestingNode = requestingNode;
		this.transtype = transtype;
	}
	
	public String getTransType(){
		return transtype;
	}

	public String getRequestingNode() {
		return requestingNode;
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
	
	public void setRequestingNode(String rn)
	{
		this.requestingNode = rn;
	}
	
	public void setTransType(String transtype)
	{
		this.transtype = transtype;
	}
	

}
