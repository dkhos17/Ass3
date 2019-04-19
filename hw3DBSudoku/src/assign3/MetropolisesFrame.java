package assign3;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.*;

public class MetropolisesFrame extends JFrame{
	
	private JTextField polis, cont, pop;
	private JButton add, search;
	private JComboBox<Object> compare, uniqe;
	private Metropolises data = new Metropolises();
	
	public  MetropolisesFrame() {
		super("Metropolis Viewer");
		this.setLayout(new BorderLayout());
		
		JPanel info = new JPanel();
		JPanel Fields = new JPanel();
		
		JTable table = new JTable(data);
		info.add(table);
		
		polis = new JTextField(10);
		cont = new JTextField(10);
		pop = new JTextField(10);
		
		Fields.add(new JLabel("Metropolis:"));
		Fields.add(polis);
		Fields.add(new JLabel("Continent:"));
		Fields.add(cont);
		Fields.add(new JLabel("Population:"));
		Fields.add(pop);
		
		this.add(Fields, BorderLayout.NORTH);
		this.add(info);
		
		JPanel menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		
		add = new JButton("    ADD    ");
		search = new JButton("SEARCH");
		
		menu.add(new JLabel("\n\n"));
		menu.add(add);
		menu.add(search);
		menu.add(new JLabel("Search Options"));
		
		this.add(menu, BorderLayout.EAST);
		
		String[] args0 = {"Population", "Population Larger Than", "Population Less Than"};
		compare = new JComboBox<Object>(args0);
		String[] args1 = {"Match", "Exact Match", "Partial Match"};
		uniqe = new JComboBox<Object>(args1);
		
		menu.add(new JLabel("\n\n"));
		menu.add(compare);
		menu.add(new JLabel("\n\n"));
		menu.add(uniqe);
		menu.add(new JLabel("\n\n"));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void addListeners() {
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String src1 = polis.getText(), src2 = cont.getText(), src3 = pop.getText();
				data.addRow(Arrays.asList(src1, src2, src3));
				try { data.insert(Arrays.asList(src1, src2, src3));} catch(SQLException e1) {}
			}
		});
		
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String src1 = polis.getText(), src2 = cont.getText(), src3 = pop.getText();
				try { data.select(src1, src2, src3);} catch (SQLException e1) {}
			}
		});
	}
	
	public static void main(String[] args) {
		MetropolisesFrame MFrame = new MetropolisesFrame();
	}

}
