package assign3;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;


public class MetroPolicy extends JFrame{

	static String account = MyDBInfo.MYSQL_USERNAME;
	static String password = MyDBInfo.MYSQL_PASSWORD;
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;

	public  MetroPolicy() {
		super("Metropolis Viewer");
		this.setLayout(new BorderLayout(4,4));
//		setPreferredSize(new Dimension(Width, Height));
		
		JPanel info = new JPanel();
//		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		
		JTable table = new JTable(10,3);
		info.add(table);
		
		JPanel Search = new JPanel();
//		Search.setLayout(new BoxLayout(Search, BoxLayout.Y_AXIS));
		
		JTextField polis = new JTextField(10);
		JTextField cont = new JTextField(10);
		JTextField pop = new JTextField(10);
		
		Search.add(new JLabel("Metropolis:"));
		Search.add(polis);
		Search.add(new JLabel("Continent:"));
		Search.add(cont);
		Search.add(new JLabel("Population:"));
		Search.add(pop);
		
		this.add(Search, BorderLayout.NORTH);
		this.add(info);
		
		JPanel menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		
		JButton add = new JButton("ADD");
		JButton search = new JButton("SEARCH");
		
		menu.add(new JLabel("\n\n"));
		menu.add(add);
		menu.add(search);
		menu.add(new JLabel("Search Options"));
		
//		menu.add(m);
		this.add(menu, BorderLayout.EAST);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	public static void main(String[] args) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://"
					+ server, account, password);
			//Connection con = ConnectionPool.getInstance().getConnectionFromPool();

			Statement stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
			stmt.executeUpdate("INSERT INTO metropolises VALUES ('Tbilisi', 'Europe', 1580000)");
			stmt.executeUpdate("DELETE FROM metropolises where metropolis='Tbilisi'");
			
			/*
			PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO metropolises VALUES(?,?,?)");
			preparedStatement.setString(1, "Tbilisi");
			preparedStatement.setString(2, "Europe");
			preparedStatement.setInt(3, 1580000);
			preparedStatement.execute();
			*/
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM metropolises");

			while (rs.next()) {
				String name = rs.getString("metropolis");
				long pop = rs.getLong("population");
				System.out.println(name + "\t" + pop);
			}
			
			MetroPolicy frame = new MetroPolicy();
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
