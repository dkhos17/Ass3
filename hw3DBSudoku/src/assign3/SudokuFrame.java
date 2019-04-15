package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	
//	 private static int Width = 1200, Height = 1000;
	 private JTextArea puzzText, solText; 
	 private JCheckBox AutoCheck;
	 private JButton Check;
	 private Document auto;
	 
	public SudokuFrame() {
		super("Sudoku Solver");
		this.setLayout(new BorderLayout(4,4));
//		setPreferredSize(new Dimension(Width, Height));
		
		JPanel LPanel = new JPanel();
		JPanel RPanel = new JPanel();
		JPanel DPanel = new JPanel();
		
		puzzText = new JTextArea(40, 40);
		puzzText.setBorder(new TitledBorder("Puzzle"));	
		puzzText.setEditable(true);
		
		solText = new JTextArea(40, 40);
		solText.setBorder(new TitledBorder("Solution"));
		solText.setEditable(false);
		
		AutoCheck = new JCheckBox();
		Check = new JButton("Check");
		auto = puzzText.getDocument();
		
		LPanel.add(puzzText);
		RPanel.add(solText);
		DPanel.add(Check);
		DPanel.add(AutoCheck);
		
		addListeners();
		
		this.add(LPanel, BorderLayout.WEST);
		this.add(RPanel, BorderLayout.EAST);
		this.add(DPanel, BorderLayout.SOUTH);
		
		// Could do this:
		// setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void addListeners() {
		Check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					SudokuSolution();
			}
		});
		
		auto.addDocumentListener( new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if(AutoCheck.isSelected())
					SudokuSolution();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(AutoCheck.isSelected())
					SudokuSolution();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if(AutoCheck.isSelected())
					SudokuSolution();
			}
		});
	}
	
	private void SudokuSolution() {
		try {
			solText.setText("Loading...");
			Sudoku sudo = new Sudoku(puzzText.getText());
			int count = sudo.solve();
			String solution = sudo.getSolutionText();
			solution += "solution: " + count + '\n';
			solution += "elapsed: " + sudo.getElapsed() + "ms";
			solText.setText(solution);
		} catch (Exception e) {}//throw e;
	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
