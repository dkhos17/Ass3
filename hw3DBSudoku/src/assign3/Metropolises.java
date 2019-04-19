package assign3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;


public class Metropolises extends AbstractTableModel{

	static String account = MyDBInfo.MYSQL_USERNAME;
	static String password = MyDBInfo.MYSQL_PASSWORD;
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;
	
	private List <String> colNames;
    private List <List<String>> data;

    private static Statement stmt; 
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

			stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void insert(List<String> args) throws SQLException {
		stmt.executeUpdate("INSERT INTO metropolises VALUES" + args.toString());
	}
	
	public void select(String m, String c, String p) throws SQLException {
		stmt.executeUpdate("SELECT * FROM metropolises WHERE"
				+ (m.isEmpty() ? "" : ("metropolis = " + m + " and"))
				+ (c.isEmpty() ? "" : ("continent = " + c + " and"))
				+ (p.isEmpty() ? "" : ("population = " + p )));
	}
	
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
