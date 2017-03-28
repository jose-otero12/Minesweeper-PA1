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
	public boolean[][] clickedGrids = new boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	
	
	public Rectangle2D.Double restartButton = new Rectangle2D.Double(180, 350, 40, 40);
	
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		this.setLayout(null);
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
		labels();
		
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
		g.setColor(Color.GRAY);
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
	
	public void reset() { 

		clear();
		
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}

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
		removeAll();
		adjacentMines = adjacentMines();
		labels();
		repaint();
		
	}
	

	public void clear() {
		for (int i = 0; i < TOTAL_COLUMNS; i++) {
			for (int j = 0; j < TOTAL_ROWS; j++) {
				mines[i][j] = 0;

				clickedGrids[i][j]=false;

				clickedGrids[i][j] = false;

			}
		}
	}
	
	public void labels() {
		JLabel label;
		for (int i = 1; i <= TOTAL_COLUMNS; i++) {
			for (int j = 1; j <= TOTAL_ROWS; j++) {
				if (!this.isMine(i-1,j-1) && adjacentMines[i-1][j-1] > 0 ) {
					label = new JLabel(String.valueOf(adjacentMines[i-1][j-1]), JLabel.CENTER);
					label.setSize(29, 29);
					add(label);

					int cx = 55 + 30*(i-1);//INNER_CELL_SIZE/2*i;
					int cy = 40 + 30*(j-1);//INNER_CELL_SIZE/2*j;
					label.setLocation(cx,cy);
					System.out.println("Location: "+ label.getLocation());
					label.setVisible(false);
					labels[i-1][j-1] = label;
				}
			}
		}
	}
	
	private boolean hasAdjacent(int x, int y){		
		return adjacentMines[x][y]>0;		
	}
	
	public void dominoEffect(int col, int row){
		
		for(int i = col-1;i<=col+1;i++){
			for(int j = row-1;j<=row+1;j++){
				System.out.println("indexes: "+ i+" "+j);
				
					if(isValid(i,j) && hasAdjacent(i,j) && !isMine(i,j) && !isClicked(i,j)){	
						clickAT(i,j);
						colorArray[i][j] = Color.LIGHT_GRAY;
						labels[i][j].setVisible(true);
					}
					else if(isValid(i,j) && !isMine(i,j) && !isClicked(i,j)){	
						clickAT(i,j);
						colorArray[i][j] = Color.LIGHT_GRAY;
						dominoEffect(i,j);
					}

			}
		}
		repaint();
	}
	
	public void clickAT(int col, int row){
		clickedGrids[col][row]=true;		
	}
	public boolean isClicked(int col, int row){
		return clickedGrids[col][row];		
	}
	public JLabel[][] getLabels() {
		return labels;
	}

	public void setLabels(JLabel[][] labels) {
		this.labels = labels;
	}
	
	public JLabel getLabelAt(int col, int row) {
		return labels[col][row];
	}
	
	public void setLabelAt(int col, int row, JLabel newLabel) {
		labels[col][row] = newLabel;
	}
	
	public boolean isFinished() {

		for (int i = 0; i < TOTAL_COLUMNS; i++) {
			for (int j = 0; j < TOTAL_ROWS; j++) {
				if (mines[i][j] != 1 && !isClicked(i,j)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void endGameResult(boolean winOrLose) {
		if (winOrLose) {
			JLabel label = new JLabel("Congratulations, you won!", JLabel.CENTER);
			label.setSize(200, 30);
			add(label);
			label.setLocation(10,10);
			for(int i = 0; i < (MyPanel.getTotalColumns()); i++){
				for(int j = 0; j < (MyPanel.getTotalRows()); j++){
					Color black = Color.BLACK;

					if(this.mines[i][j] == 1){
						this.colorArray[i][j] = black;
						this.repaint();
					}

				}

			}
		}
		else {
			JLabel label = new JLabel("Sorry, try again.", JLabel.CENTER);
			label.setSize(200, 30);
			add(label);
			label.setLocation(10,10);
		}
		
		JLabel resetMessege = new JLabel("Reset", JLabel.CENTER);
		resetMessege.setSize(40, 40);
		add(resetMessege);
		resetMessege.setLocation(180,350);
	}
 }