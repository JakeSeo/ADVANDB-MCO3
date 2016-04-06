package model;

import java.io.Serializable;

public class Transaction implements Serializable{
	private String name;
	private String query;
	private String writeQuery;
	private int isolationLvl;
	private int end;
	private String database;
	private String transtype;
	private String nodeType;
	
	public Transaction(String name, String query, int isolationLvl, int end, String database, String transtype) {
		this.name = name;
		this.isolationLvl = isolationLvl;
		this.end = end;
		this.query = query;
		this.database = database;
		this.transtype = transtype;
		this.nodeType = nodeType;
		this.writeQuery = query;
	}
	
	public String getWriteQuery(){
		return writeQuery;
	}
	
	public String getNodeType(){
		return nodeType;
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

	public void setNodeType(String NodeType){
		this.nodeType = NodeType;
	}
}
