import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 9;   
	private static final int TOTAL_MINES = 10;

	private Random generator = new Random();
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public boolean gameStart = false;
	public int firstXCoordinate;
	public int firstYCoordinate;

	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS]; //Sets the color of each cell
	public Boolean[][] minesArray = new Boolean[TOTAL_COLUMNS][TOTAL_ROWS]; //Array that stores whether a cell contains a mine
	public int[][] numberOfMines = new int[TOTAL_COLUMNS][TOTAL_ROWS]; //Array that assigns to each cell the number of mines adjacent to it

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
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //9x9 grid is white
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}
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

		//By default, the grid will be 9x9 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS)));
		}

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				Color c = colorArray[x][y];
				g.setColor(c);
				g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
			}
		}
		//Draws the number of adjacent mines to the cell.
		drawNumbers(g); 
	}

	//Method that sets mines in 10 random squares
	public void setMines() {
		//Initialization of array
		for (int i = 0; i < TOTAL_COLUMNS; i++) {
			for (int k = 0; k < TOTAL_ROWS; k++) {
				minesArray[i][k] = false; 
			}
		}
		int xBombCoordinate;
		int yBombCoordinate;
		int setMines = 0;
		while(setMines < TOTAL_MINES) {
			xBombCoordinate = generator.nextInt(TOTAL_COLUMNS);
			yBombCoordinate = generator.nextInt(TOTAL_ROWS);
			if(xBombCoordinate != firstXCoordinate && yBombCoordinate != firstYCoordinate) {
			if(!minesArray[xBombCoordinate][yBombCoordinate]) {
				setMines++;
				minesArray[xBombCoordinate][yBombCoordinate] = true;
				numberOfMines[xBombCoordinate][yBombCoordinate] = -1; //-1 indicates that there is a mine in that cell
				}
			}
		}
		nearMines();
	}

	public void nearMines(){ //Checks the number of adjacent mines to the cell and stores it in numberOfMines array.
		for (int i = 0; i < TOTAL_COLUMNS; i++) {
			for (int j = 0; j < TOTAL_ROWS; j++) {
				if (numberOfMines[i][j] != -1){  
					numberOfMines[i][j] = 0;
					if (j >= 1 && numberOfMines[i][j-1] == -1) 
						numberOfMines[i][j]++;
					if (j <= TOTAL_ROWS-2 && numberOfMines[i][j+1] == -1) 
						numberOfMines[i][j]++;
					if (i >= 1 && numberOfMines[i-1][j] == -1) 
						numberOfMines[i][j]++;
					if (i <=  TOTAL_COLUMNS-2 && numberOfMines[i+1][j] == -1) 
						numberOfMines[i][j]++;
					if (i >= 1 && j >= 1 && numberOfMines[i-1][j-1]  == -1) 
						numberOfMines[i][j]++;
					if (i <= TOTAL_COLUMNS-2 && j >= 1 && numberOfMines[i+1][j-1] == -1) 
						numberOfMines[i][j]++;
					if (i <= TOTAL_COLUMNS-2 && j <= TOTAL_ROWS-2 && numberOfMines[i+1][j+1] == -1) 
						numberOfMines[i][j]++;
					if (i >= 1 && j <= TOTAL_ROWS-2 && numberOfMines[i-1][j+1] == -1) 
						numberOfMines[i][j]++;
				}
			}
		}	
	}

	public void paintAdjacentCells(int i, int j) { //Checks what cells around the clicked cell are empty (no mine) and paints them
		i = mouseDownGridX;
		j = mouseDownGridY;

		if (numberOfMines[i][j] == 0) {
			if (j >= 1) {
				if(!minesArray[i][j-1] && colorArray[i][j-1] != Color.RED) {
					colorArray[i][j-1] = Color.GRAY;
					traceBlankPath(i, j-1);
				}
			}
			if (j <= TOTAL_ROWS - 2) {
				if(!minesArray[i][j+1] && colorArray[i][j+1] != Color.RED) {
					colorArray[i][j+1] = Color.GRAY;
					traceBlankPath(i, j+1);
				}
			}
			if (i >= 1) {
				if(!minesArray[i-1][j] && colorArray[i-1][j] != Color.RED) {
					colorArray[i-1][j] = Color.GRAY;
					traceBlankPath(i-1, j);
				}
			}
			if (i <= TOTAL_COLUMNS - 2) {
				if(!minesArray[i+1][j] && colorArray[i+1][j] != Color.RED) {
					colorArray[i+1][j] = Color.GRAY;
					traceBlankPath(i+1, j);
				}
			}
			if (i >= 1 && j >= 1) { 
				if(!minesArray[i-1][j-1] && colorArray[i-1][j-1] != Color.RED) {
					colorArray[i-1][j-1] = Color.GRAY;
					traceBlankPath(i-1, j-1);
				}
			}
			if (i <= TOTAL_COLUMNS - 2 && j >= 1) {
				if(!minesArray[i+1][j-1] && colorArray[i+1][j-1] != Color.RED) {
					colorArray[i+1][j-1] = Color.GRAY;
					traceBlankPath(i+1, j-1);
				}
			}
			if (i <= TOTAL_COLUMNS - 2 && j <= TOTAL_ROWS - 2) {
				if(!minesArray[i+1][j+1] && colorArray[i+1][j+1] != Color.RED) {
					colorArray[i+1][j+1] = Color.GRAY;
					traceBlankPath(i+1, j+1);
				}
			}
			if (i >= 1 && j <= TOTAL_ROWS - 2) {
				if(!minesArray[i-1][j+1] && colorArray[i-1][j+1] != Color.RED) {
					colorArray[i-1][j+1] = Color.GRAY;
					traceBlankPath(i-1, j+1);
				}
			}
		} 
	}

	public void traceBlankPath(int xCoordinate, int yCoordinate) {
		for(int m = 1; xCoordinate + m < TOTAL_COLUMNS; m++) {
			if(colorArray[xCoordinate + m][yCoordinate] == Color.GRAY) {
				break;
			}
			if(numberOfMines[xCoordinate + m][yCoordinate] == 0 && colorArray[xCoordinate + m][yCoordinate] != Color.RED) { //Moving to the right
				colorArray[xCoordinate + m][yCoordinate] = Color.GRAY;
			} else {
				if(m > 1 && numberOfMines[xCoordinate + m][yCoordinate] > 0 && colorArray[xCoordinate + m][yCoordinate] != Color.RED) {
					colorArray[xCoordinate + m][yCoordinate] = Color.GRAY; 
				}
				break;
			}
		}
		for(int m = 1; xCoordinate - m >= 0; m++) {
			if(colorArray[xCoordinate - m][yCoordinate] == Color.GRAY) {
				break;
			}
			if(numberOfMines[xCoordinate - m][yCoordinate] == 0 && colorArray[xCoordinate - m][yCoordinate] != Color.RED) { //Moving to the left
				colorArray[xCoordinate - m][yCoordinate] = Color.GRAY;
			} else {
				if(m > 1 && numberOfMines[xCoordinate - m][yCoordinate] > 0 && colorArray[xCoordinate - m][yCoordinate] != Color.RED) {
					colorArray[xCoordinate - m][yCoordinate] = Color.GRAY; 
				}
				break;
			}
		}
		for(int k = 1; yCoordinate + k < TOTAL_ROWS; k++) {
			if(colorArray[xCoordinate][yCoordinate + k] == Color.GRAY) {
				break;
			}
			if(numberOfMines[xCoordinate][yCoordinate + k] == 0 && colorArray[xCoordinate][yCoordinate + k] != Color.RED) { //Moving down
				colorArray[xCoordinate][yCoordinate + k] = Color.GRAY;
			} else {
				if(k > 1 && numberOfMines[xCoordinate][yCoordinate + k] > 0 && colorArray[xCoordinate][yCoordinate + k] != Color.RED) {
					colorArray[xCoordinate][yCoordinate + k] = Color.GRAY; 
				}
				break;
			}
		}
		for(int k = 1; yCoordinate - k >= 0; k++) {
			if(colorArray[xCoordinate][yCoordinate - k] == Color.GRAY) {
				break;
			}
			if(numberOfMines[xCoordinate][yCoordinate - k] == 0 && colorArray[xCoordinate][yCoordinate - k] != Color.RED) { //Moving up
				colorArray[xCoordinate][yCoordinate - k] = Color.GRAY;
			} else {
				if(k > 1 && numberOfMines[xCoordinate][yCoordinate - k] > 0 && colorArray[xCoordinate][yCoordinate - k] != Color.RED) {
					colorArray[xCoordinate][yCoordinate - k] = Color.GRAY;
				}
				break;
			}
		}

		//Moving in the diagonals
		for(int k = 1; yCoordinate - k >= 0; k++) {
			if (xCoordinate - k >= 0){
				if(colorArray[xCoordinate-k][yCoordinate - k] == Color.GRAY) {
					break;
				}
				if(numberOfMines[xCoordinate-k][yCoordinate - k] == 0 && colorArray[xCoordinate - k][yCoordinate - k] != Color.RED) { 
					colorArray[xCoordinate-k][yCoordinate - k] = Color.GRAY;
				} else {
					if(k > 1 && numberOfMines[xCoordinate-k][yCoordinate - k] > 0 && colorArray[xCoordinate - k][yCoordinate - k] != Color.RED) {
						colorArray[xCoordinate-k][yCoordinate - k] = Color.GRAY; 
					}
					break;
				}
			}
		}
		for(int k = 1; yCoordinate - k >= 0; k++) {
			if (xCoordinate + k < TOTAL_COLUMNS){
				if(colorArray[xCoordinate+k][yCoordinate - k] == Color.GRAY) {
					break;
				}
				if(numberOfMines[xCoordinate+k][yCoordinate - k] == 0 && colorArray[xCoordinate + k][yCoordinate - k] != Color.RED) { 
					colorArray[xCoordinate+k][yCoordinate - k] = Color.GRAY;
				} else {
					if(k > 1 && numberOfMines[xCoordinate+k][yCoordinate - k]>0 && colorArray[xCoordinate + k][yCoordinate - k] != Color.RED) {
						colorArray[xCoordinate+k][yCoordinate - k] = Color.GRAY; 
					}
					break;
				}
			}
		}
		for(int k = 1; yCoordinate + k < TOTAL_ROWS; k++) {
			if(xCoordinate + k < TOTAL_COLUMNS){
				if(colorArray[xCoordinate+k][yCoordinate + k] == Color.GRAY) {
					break;
				}
				if(numberOfMines[xCoordinate+k][yCoordinate + k] == 0 && colorArray[xCoordinate + k][yCoordinate + k] != Color.RED) { 
					colorArray[xCoordinate+k][yCoordinate + k] = Color.GRAY;
				} else {
					if(k > 1 && numberOfMines[xCoordinate+k][yCoordinate + k]>0 && colorArray[xCoordinate + k][yCoordinate + k] != Color.RED) {
						colorArray[xCoordinate+k][yCoordinate + k] = Color.GRAY; 
					}
					break;
				}
			}
		}
		for(int k = 1; yCoordinate + k < TOTAL_ROWS; k++) {
			if(xCoordinate - k >= 0){
				if(colorArray[xCoordinate-k][yCoordinate + k] == Color.GRAY) {
					break;
				}
				if(numberOfMines[xCoordinate-k][yCoordinate + k] == 0 && colorArray[xCoordinate - k][yCoordinate + k] != Color.RED) { 
					colorArray[xCoordinate-k][yCoordinate + k] = Color.GRAY;
				} else {
					if(k > 1 && numberOfMines[xCoordinate-k][yCoordinate + k] > 0 && colorArray[xCoordinate - k][yCoordinate + k] != Color.RED) {
						colorArray[xCoordinate-k][yCoordinate + k] = Color.GRAY; 
					}
					break;
				}
			}
		}
	}

	public void firstClick(int xCoordinate, int yCoordinate){ //Determines when the setMines method can be called, after the first click.
		firstXCoordinate = xCoordinate;
		firstYCoordinate = yCoordinate;
		gameStart = true;
	}
	
	public void lostGame() {
		for (int i = 0; i < TOTAL_COLUMNS; i++) {
			for (int k = 0; k < TOTAL_ROWS; k++) {
				if(minesArray[i][k]) {
					colorArray[i][k] = Color.BLACK;
					repaint();
				}
			}
		}
		JOptionPane.showMessageDialog(null, "Hit a mine; lost the game!");
	}
	
	public void wonGameCondition() { 
		int m = 0;
		for (int i = 0; i < TOTAL_COLUMNS; i++) {
			for (int j = 0; j < TOTAL_ROWS; j++) {
				if(!minesArray[i][j] && colorArray[i][j] == Color.GRAY) {
					m++;
				}
			}
		}
		if(m == ((TOTAL_ROWS)*(TOTAL_COLUMNS)) - TOTAL_MINES) {
			JOptionPane.showMessageDialog(null, "Won the game!");

		}
	}

	public void drawNumbers(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (int i=0; i < TOTAL_COLUMNS; i++){
			for (int j=0; j < TOTAL_ROWS; j++){
				if ((numberOfMines[i][j] == 1) && (colorArray[i][j] == Color.GRAY)){
					g2.setColor(Color.BLUE);
					g2.drawString(numberOfMines[i][j]+"", i*INNER_CELL_SIZE+40, j*INNER_CELL_SIZE+50);
				}
				if ((numberOfMines[i][j] == 2) && (colorArray[i][j] == Color.GRAY)){
					g2.setColor(Color.GREEN);
					g2.drawString(numberOfMines[i][j]+"", i*INNER_CELL_SIZE+40, j*INNER_CELL_SIZE+50);
				}
				if ((numberOfMines[i][j] == 3) && (colorArray[i][j] == Color.GRAY)){
					g2.setColor(Color.YELLOW);
					g2.drawString(numberOfMines[i][j]+"", i*INNER_CELL_SIZE+40, j*INNER_CELL_SIZE+50);
				}
				if ((numberOfMines[i][j] == 4) && (colorArray[i][j] == Color.GRAY)){
					g2.setColor(Color.MAGENTA);
					g2.drawString(numberOfMines[i][j]+"", i*INNER_CELL_SIZE+40, j*INNER_CELL_SIZE+50);
				}
				if ((numberOfMines[i][j] > 4) && (colorArray[i][j] == Color.GRAY)){
					g2.setColor(Color.BLACK);
					g2.drawString(numberOfMines[i][j]+"", i*INNER_CELL_SIZE+40, j*INNER_CELL_SIZE+50);
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
		if (x < 0 || x > TOTAL_COLUMNS || y < 0 || y > TOTAL_ROWS) {   //Outside the rest of the grid
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
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
}