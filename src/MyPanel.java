import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.javafx.geom.Rectangle;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 55;
	private static final int GRID_Y = 40;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 9;
	private int[][] adjacentMines;
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int numberOfMines = 10;
	public int[][] mines = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	public JLabel labels[][] = new JLabel[TOTAL_COLUMNS][TOTAL_ROWS];
	
	
	public Rectangle2D.Double restartButton = new Rectangle2D.Double(180, 350, 40, 40);
	
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
////
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //the grid
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}
		// Assigns the mines to different places
		for (int i = 0; i < numberOfMines; i++ ){
			int randX = new Random().nextInt(TOTAL_COLUMNS);
			int randY = new Random().nextInt(TOTAL_ROWS);

			if (mines[randX][randY]!=1){
			mines[randX][randY] = 1;
			}
			else {
				i--;
			}
		}
		adjacentMines = adjacentMines();
		
//		for (int i = 0; i < TOTAL_COLUMNS; i++) {
//			for (int j = 0; j < TOTAL_ROWS; j++) {
//				labels[i][j].setText(String.valueOf(adjacentMines[i][j]));
//				labels[i][j].setLocation(i, j);
//				labels[i][j].setSize(29, 29);
//			}
//		}
	}
	
	private boolean isValid(int col, int row) {
		return ((col >= 0 && col < TOTAL_COLUMNS) && (row >= 0 && row < TOTAL_ROWS));
	}
	
	private boolean isMine(int col, int row) {
		return (mines[col][row] == 1);
	}
	
	//Assigns number of adjacent mines to empty grids.
	public int[][] adjacentMines() {
		int adjacent[][] = new int[TOTAL_COLUMNS][TOTAL_ROWS];
		int counter = 0;
		for (int i=0; i < TOTAL_COLUMNS; i++) {
			for (int j=0; j < TOTAL_ROWS; j++) {
				if (!isMine(i,j)) {
					if (isValid(i-1,j-1) && isMine(i-1,j-1)) {
						counter++;
					}
					if (isValid(i,j-1) && isMine(i,j-1)) {
						counter++;
					}
					if (isValid(i+1,j-1) && isMine(i+1,j-1)) {
						counter++;
					}
					if (isValid(i-1,j) && isMine(i-1,j)) {
						counter++;
					}
					if (isValid(i+1,j) && isMine(i+1,j)) {
						counter++;
					}
					if (isValid(i-1,j+1) && isMine(i-1,j+1)) {
						counter++;
					}
					if (isValid(i,j+1) && isMine(i,j+1)) {
						counter++;
					}
					if (isValid(i+1,j+1) && isMine(i+1,j+1)) {
						counter++;
					}
					adjacent[i][j] = counter;
					counter = 0;
					
					System.out.println("Adjacen Mines: " + adjacent[i][j]);
				}
			}
		}

		return adjacent;
	}
	
	public int[][] getAdjacentMines() {
		return adjacentMines;
	}
	
	public int getAdjacentMines(int col, int row) {
		return adjacentMines[col][row];
	}

	public void setAdjacentMines(int[][] adjacentMines) {
		this.adjacentMines = adjacentMines;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.YELLOW);
		g2.fill(restartButton);
		g2.setColor(Color.BLACK);
		g2.draw(restartButton);

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS ; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS )));
		}

		//Draw an additional cell at the bottom left
	//	g.drawRect(x1 + GRID_X, y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)), INNER_CELL_SIZE + 1, INNER_CELL_SIZE + 1);

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((x == 0) || (y != TOTAL_ROWS )) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}
	}
	
	
	
	
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1 ) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS ) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
	
	public static int getTotalRows(){
		
		return TOTAL_ROWS;
	}
	public static int getTotalColumns(){
		
		return TOTAL_COLUMNS;
	}
}