package assign3;

import java.util.*;

import assign3.Sudoku.Triple;
//import  javafx.util.*;


/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	
	public static class Triple {
        public int x,y,v;
        Triple(int x, int y, int value){
        	this.x = x; this.y = y; this.v = value;
        }
    }
	public class SortByValue implements Comparator<Triple> {
			@Override
			public int compare(Triple o1, Triple o2) {
				if(o1.v != o2.v) return o1.v - o2.v;
				if(o1.x != o2.x) return o1.x - o2.x;
				return o1.y - o2.y;
			}
     }
	
	private int[][] sudo, sol;
	private ArrayList<Triple> points; 
	private int filled, total_ways;
	private long timer;
	
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	//evil grid with one solution
	public static final int[][] evilGrid = Sudoku.stringsToGrid(
			"0 0 0 0 1 0 0 0 6",
			"5 9 7 0 0 0 0 0 0",
			"2 0 0 5 8 0 0 0 0",
			"0 8 0 0 0 0 9 0 0",
			"4 0 0 7 0 3 0 0 1",
			"0 0 2 0 0 0 0 7 0",
			"0 0 0 0 4 9 0 0 2",
			"0 0 0 0 0 0 3 1 5",
			"1 0 0 0 2 0 0 0 0");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(evilGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		sudo = ints;
		sol = new int[SIZE][SIZE];
		total_ways = filled = 0;
		points = new ArrayList<Triple>();
		getPoints();
	}
	
	public Sudoku(String ints) {
		this(textToGrid(ints));
	}
	
	
	//get unfilled points
	private void getPoints() {
		try {
			for(int i = 0; i < SIZE; i++) {
				for(int j = 0; j < SIZE; j++) {
					if(sudo[i][j] != 0) {
//						in case we neww to check that given sudoku is correct
						if(!isCorr(i, j, sudo[i][j]))
							throw new RuntimeException ("Invalid Puzzle!");
						filled++;  continue;
					}
					Triple p = new Triple(i,j,0);
					for(int v = 1; v < 10; v++) 
						if(canBe(i, j, v)) p.v++;
					if(p.v != 0) points.add(p);
				}
			}
			Collections.sort(points, new SortByValue() );
		} catch (Exception e) {
			throw new RuntimeException ("Invalid Puzzle!");
		}
	}
	
	
	//check (i,j) is correct or not
	private boolean isCorr(int x, int y, int val) {
		for(int i = 0; i < SIZE; i++) {
			if(i != y && sudo[x][i] == val) return false;
			if(i != x && sudo[i][y] == val) return false;
		}
		for(int i = PART*(x/PART); i < PART*(x/PART) + PART; i++) {
			for(int j = PART*(y/PART); j < PART*(y/PART) + PART; j++) {
				if(i == x && j == y) continue;
				if(sudo[i][j] == val) return false;
			}
		}
		return true;
	}


	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		if(filled + points.size() != SIZE*SIZE) return 0;
		timer = -System.currentTimeMillis();
		solveSudoku(0);
		timer += System.currentTimeMillis();
		return total_ways;
	}
	
	private void solveSudoku(int idx) {
		if(total_ways >= MAX_SOLUTIONS) return;
		if(filled == SIZE*SIZE) {
			if(total_ways == 0) copySolution();
			total_ways++;  return;
		}
		Triple bst = points.get(idx); 
		for(int v = 1; v <= SIZE; v++) {
			if(canBe(bst.x, bst.y, v)) {
				sudo[bst.x][bst.y] = v;
				filled++;
				solveSudoku(idx+1);
				sudo[bst.x][bst.y] = 0;
				--filled;
			}
		}	
	}
	
	//get and copy first solution
	private void copySolution() {
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				sol[i][j] = sudo[i][j];
			}
		}
	}

	//check val if it can be placed in (i,j)  
	private boolean canBe(int x, int y, int val) {
		if(sudo[x][y] != 0) return false;
		for(int i = 0; i < SIZE; i++) {
			if(sudo[x][i] == val) return false;
			if(sudo[i][y] == val) return false;
 		}
		for(int i = PART*(x/PART); i < PART*(x/PART) + PART; i++) {
			for(int j = PART*(y/PART); j < PART*(y/PART) + PART; j++) {
				if(sudo[i][j] == val) return false;
			}
		}
		return true;
	}
	

	
	public String getSolutionText() {
		String s = "";
		for(int i = 0; i < SIZE; i++) s += Arrays.toString(sol[i]) + '\n';
		return s;
	}
	
	public String toString() {
		String s = "";
		for(int i = 0; i < SIZE; i++) s += Arrays.toString(sudo[i]) + '\n';
		return s;
	}
	
	public long getElapsed() {
		return timer;
	}
	
}
