package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import com.mysql.jdbc.PreparedStatement;
import com.sun.rowset.CachedRowSetImpl;

public class DBModel {
	private Connection conn = DBConnection.getConnection();
	private PreparedStatement statement;
	private ResultSet rs;
	private CachedRowSetImpl cs;

	private static DBModel dbm = null;

	public static synchronized DBModel getInstance() {
		if (dbm == null) {
			dbm = new DBModel();

		}

		return dbm;
	}

	public TableContents getData(String transactionName, String q) {
		rs = null;
		cs = null;
		TableContents tc = null;
		try {
			String query = q;
			statement = (PreparedStatement) conn.prepareStatement(query);
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
			System.out.println("DONE EXECUTING TRANS 2 " + query);
			return tc;
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} finally {
		}

		return null;
	}

	public CachedRowSetImpl getAllData() {
		rs = null;
		cs = null;
		try {
			String query = "SELECT * FROM tbl;";
			statement = (PreparedStatement) conn.prepareStatement(query);
			rs = statement.executeQuery();
			cs = new CachedRowSetImpl();
			cs.populate(rs);
			while (true) {
				if (cs != null)
					break;
			}
			System.out.println("DONE EXECUTING TRANS 2 " + query);
			return cs;
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

		return cs;

	}

	public void updateData(int id, int value) {

		try {
			String query = "UPDATE TABLE tbl SET num1 = ? WHERE id = ?";
			statement = (PreparedStatement) conn.prepareStatement(query);
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

}