package assign3;


import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

public class MetropolisesFrame extends JFrame{
	
	private JTextField polis, cont, pop;
	private JButton add, search;
	private JComboBox<Object> compare, uniqe;
	private Metropolises data = new Metropolises();
	
	public  MetropolisesFrame() {
		super("Metropolis Viewer");
		this.setLayout(new BorderLayout());
		JPanel info = new JPanel();
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		
		JTable table = new JTable(data); 
		String[] columns = {"Metropolises", "Continent", "Population"};
		for (int i = 0; i < data.getColumnCount(); i++) {
			TableColumn tc = table.getColumnModel().getColumn(i);
			tc.setHeaderValue(columns[i]);
		}
//		info.add(table);
		JScrollPane sp = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		info.add(sp);
		
		polis = new JTextField(10);
		cont = new JTextField(10);
		pop = new JTextField(10);
		
		JPanel Fields = new JPanel();
		Fields.add(new JLabel("Metropolis:"));
		Fields.add(polis);
		Fields.add(new JLabel("Continent:"));
		Fields.add(cont);
		Fields.add(new JLabel("Population:"));
		Fields.add(pop);
		
		this.add(Fields, BorderLayout.NORTH);
		this.add(info, BorderLayout.CENTER);
		
		JPanel menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.setBorder(new EmptyBorder(new Insets(10, 10, 240, 10)));
		
		add = new JButton("    ADD    ");
		search = new JButton("SEARCH");
		
		String[] args0 = {"Population", "Population Larger Than", "Population Smaller Than"};
		compare = new JComboBox<Object>(args0);
		String[] args1 = {"Exact Match", "Partial Match"};
		uniqe = new JComboBox<Object>(args1);
		
		addListeners(args0, args1);
		menu.add(new JLabel("\n\n"));
		menu.add(add);
		menu.add(search);
		menu.add(new JLabel("Search Options"));
		
		menu.add(new JLabel("\n\n"));
		menu.add(compare);
		menu.add(new JLabel("\n\n"));
		menu.add(uniqe);
		menu.add(new JLabel("\n\n"));
		
		this.add(menu, BorderLayout.EAST);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	//this method adds ActionListeners on add and search buttons
	private void addListeners(String[] args0, String[] args1) {
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String src1 = polis.getText(), src2 = cont.getText(), src3 = pop.getText();
				try {
					data.insert(src1, src2, src3);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String src1 = polis.getText(), src2 = cont.getText(), src3 = pop.getText();
				boolean match = args1[1].equals((String)uniqe.getSelectedItem());
				
				int larger = 0;
				if(args0[1].equals((String)compare.getSelectedItem())) larger = 1;
				else if(args0[2].equals((String)compare.getSelectedItem())) larger = -1;
				
				try {
					data.select(src1, src2, src3, match, larger);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String[] args) {
		MetropolisesFrame MFrame = new MetropolisesFrame();
	}

}
