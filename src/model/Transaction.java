package model;

import java.io.Serializable;

public class Transaction implements Serializable{
	private String name;
	private String query;
	private int isolationLvl;
	private int end;
	
	public Transaction(String name, String query, int isolationLvl, int end) {
		this.name = name;
		this.isolationLvl = isolationLvl;
		this.end = end;
		this.query = query;
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
	
	

}
