package model;

import java.io.Serializable;
import java.sql.SQLException;

import com.sun.rowset.CachedRowSetImpl;

public class TableContents implements Serializable {
	private String transactionName;
        private String transType;

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
	private CachedRowSetImpl data;
	private String[] columnNames;

	public TableContents(String transactionName, String[] columnNames, CachedRowSetImpl data) {
		this.transactionName = transactionName;
		this.data = data;
		this.columnNames = columnNames;
	}

	public CachedRowSetImpl getData() {
		// return cursor to the position before the first row
		try {
			data.beforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public String[] getColumnNames() {
		return columnNames;
	}
	
	public String getTransactionName() {
		return transactionName;
	}

}
