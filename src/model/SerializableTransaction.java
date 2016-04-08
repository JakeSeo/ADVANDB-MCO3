



package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public class SerializableTransaction implements Serializable{
	private String name;
	private String query;
	private String writeQuery;
	private String isolationLvl;
	private String end;
	private String database;
	private String transtype;
	
	public SerializableTransaction(String name, String query, String isolationLvl, String end, String database, String transtype) {
		this.name = name;
		this.isolationLvl = isolationLvl;
		this.end = end;
		this.query = query;
		this.database = database;
		this.transtype = transtype;
		this.writeQuery = query;
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

	public String getIsolationLvl() {
		return isolationLvl;
	}

	public String getEnd() {
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
	
}
