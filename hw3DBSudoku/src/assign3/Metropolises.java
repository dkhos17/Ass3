package assign3;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class Metropolises extends AbstractTableModel{

	static String account = MyDBInfo.MYSQL_USERNAME;
	static String password = MyDBInfo.MYSQL_PASSWORD;
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;
	
	private List <String> colNames;
    private List <List<String>> data;

    private Connection con;
    
	public Metropolises() {
		try {
			colNames = new ArrayList<String>();
			data = new ArrayList<List<String>>();
			addColumn("Metropolises");
			addColumn("Continent");
			addColumn("Population");
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
			
			this.table();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//This method inserts values in connected database
	public void insert(String m, String c, String p) throws SQLException {
		if(m.length() + c.length() == 0) return;
		
		con = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
		PreparedStatement stmt = con.prepareStatement("INSERT INTO metropolises VALUES(?,?,?)");
		stmt.setString(1, m);
		stmt.setString(2, c);
		if(p.isEmpty()) stmt.setInt(3, 0);
		else stmt.setInt(3, Integer.valueOf(p));
		stmt.execute();
		con.close();
		addRow(Arrays.asList(m, c, p));
//		fireTableDataChanged();
	}
	
	//This method selects database according to given values and comands
	public void select(String m, String c, String p, boolean match, int larger) throws SQLException {
		String m0 = "metropolis", c0 = "continent", p0 = "population", larg = " = ";
		String arg1, arg2;
		if(larger == 1) larg = " > ";
		else if(larger == -1) larg = " <= ";
			
		if(match) {
			arg1 = " like '%" + m + "%'";
			arg2 = " like '%" + c + "%'";
		} else {
			arg1 = " = '" + m + "'";
			arg2 = " = '" + c + "'";
		}
		
		String selectSql = "SELECT * FROM metropolises" 
				+ (m.isEmpty() && c.isEmpty() && p.isEmpty() ? "" : " WHERE ")
				+ (m.isEmpty() ? "" : (m0 + arg1)) + (m.isEmpty() || (c.isEmpty() && p.isEmpty()) ? "" : " and ")
				+ (c.isEmpty() ? "" : (c0 + arg2)) + (c.isEmpty() || p.isEmpty() ? "" : " and ")
				+ (p.isEmpty() ? "" : (p0 + larg + p));
//		System.out.println(selectSql);
		con = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
		PreparedStatement stmt = con.prepareStatement(selectSql);
		ResultSet rs = stmt.executeQuery(selectSql);
		data.clear();
		while (rs.next()) {
			String name = rs.getString("metropolis");
			String cont = rs.getString("continent");
			long pop = rs.getLong("population");
			addRow(Arrays.asList(name, cont, Long.toString(pop)));	
		}
		con.close();
		fireTableDataChanged();
	}
	
	//This method displays current database
	public void table() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
		PreparedStatement stmt = con.prepareStatement("select * from metropolises");
		ResultSet rs = stmt.executeQuery("select * from metropolises");
		while (rs.next()) {
			String name = rs.getString("metropolis");
			String cont = rs.getString("continent");
			long pop = rs.getLong("population");
			addRow(Arrays.asList(name, cont, Long.toString(pop)));	
		}
		con.close();
		fireTableDataChanged();
	}
	
	//from seminar 10
	
	@Override
	public int getColumnCount() {
        return colNames.size();
	}


	@Override
	public int getRowCount() {
        return data.size();
	}


	@Override
	public Object getValueAt(int i, int j) {
		List<String> row = data.get(i);
        if (j < row.size()) return row.get(j);
        return null;
	}
	
	 @Override
    public void setValueAt(Object o, int i, int j) {
        List<String> row = data.get(i);
        if (j >= row.size()) {
            while (j >= row.size())
                row.add(null);
        }
        row.set(j, (String) o);
        fireTableCellUpdated(i, j);
    }

    public void addColumn(String colName) {
        colNames.add(colName);
        fireTableStructureChanged();
    }
    
    public int addRow(List<String> row) {
        data.add(row);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
        return data.size() - 1;
    }
    
    public int addRow() {
        List<String> row = new ArrayList<>();
        return addRow(row);
    }

}
