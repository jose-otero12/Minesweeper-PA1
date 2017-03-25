import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.util.Random;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;


public class MyMouseAdapter extends MouseAdapter {
	//private Random generator = new Random();
	private static int counter = 0;
	public Rectangle2D.Double restartButton = new Rectangle2D.Double(180, 350, 40, 40);
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);	
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			Component c2 = e.getComponent();
			while (!(c2 instanceof JFrame)) {
				c2 = c2.getParent();
				if (c2 == null) {
					return;
				}
			}
			myFrame = (JFrame) c2;
			myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			myInsets = myFrame.getInsets();
			x1 = myInsets.left;
			y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			x = e.getX();
			y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();

			//paints a square red meaning a flag
			myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.RED;
			myPanel.repaint();
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
//				if (myPanel.mouseDownGridX == myPanel.restartButton.getX() && myPanel.mouseDownGridY == myPanel.restartButton.getY()) {
//					for (x = 0; x < MyPanel.getTotalColumns(); x++) {
//						for (y = 0; y < MyPanel.getTotalRows(); y++) {
//							Color cA = colorArray[x][y];
//							g.setColor(cA);
//							g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
//						}
//					}
//
//				}
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed
						if (myPanel.mines[gridX][gridY] == 1) {
							for(int i = 0; i < (MyPanel.getTotalColumns()); i++){
								for(int j = 0; j < (MyPanel.getTotalRows()); j++){
									Color black = Color.BLACK;

									if(myPanel.mines[i][j] == 1){
										myPanel.colorArray[i][j] = black;
										myPanel.repaint();
									}

								}

							}
						}//
						else {
							// Verificar si hay minas alrededor de donde se hizo click y poner un un numero.
							int xMouse = myPanel.mouseDownGridX;
							int yMouse = myPanel.mouseDownGridY;
							counter = 0;
							Color lightGray = Color.GRAY;




							for (int k = xMouse - 1; k <= xMouse + 1; k++) {
								for (int j = yMouse - 1; j <= yMouse +1; j++) {		
									if (myPanel.mines[k][j] == 1) {
										counter++;
										System.out.println(counter);					
									}
								}
							}


							if(counter==0){


								for (int i = xMouse -1; i <= xMouse +1; i++ ) {
									for (int j = yMouse - 1; j <= yMouse +1; j++) {	
										for(int o = i; o>=0 && myPanel.mines[o][j]==0 ; o--){
											if (myPanel.mines[i][j] == 1) {
												counter++;
												System.out.println(counter);					
											}
											if(myPanel.mines[i][j]!=1){
												if(counter==0){
													myPanel.colorArray[o][j] = lightGray;
													myPanel.repaint();	
												}											
											}		
										}

									}
								}

								for (int i = xMouse -1; i <= xMouse +1; i++ ) {
									for (int j = yMouse - 1; j <= yMouse +1; j++) {	
										for(int p = yMouse; p>=0 && myPanel.mines[i][p]==0; p--){
											if (myPanel.mines[i][j] == 1) {
												counter++;
												System.out.println(counter);					
											}
											if(myPanel.mines[i][j]!=1){
												if(counter==0){
													myPanel.colorArray[i][p] = lightGray;
													myPanel.repaint();	
												}											
											}
										}

									}
								}

								for (int i = xMouse -1; i <= xMouse +1; i++ ) {
									for (int j = yMouse - 1; j <= yMouse +1; j++) {	
										for(int q = xMouse; q<=(MyPanel.getTotalRows()-1) && myPanel.mines[q][j]==0 ; q++){
											if (myPanel.mines[i][j] == 1) {
												counter++;
												System.out.println(counter);					
											}
											if(myPanel.mines[i][j]!=1){
												if(counter==0){
													myPanel.colorArray[q][j] = lightGray;
													myPanel.repaint();	
												}											
											}	
										}

									}
								}

								for (int i = xMouse -1; i <= xMouse +1; i++ ) {
									for (int j = yMouse - 1; j <= yMouse +1; j++) {	
										for(int r = i; r<=(MyPanel.getTotalColumns()-1) && myPanel.mines[i][r]==0; r++){
											if (myPanel.mines[i][j] == 1) {
												counter++;
												System.out.println(counter);					
											}
											if(myPanel.mines[i][j]!=1){
												if(counter==0){
													myPanel.colorArray[i][r] = lightGray;
													myPanel.repaint();	
												}											
											}			
										}


									}
								}

							}

							for(int o = myPanel.mouseDownGridX; o >= 0 && myPanel.mines[o][myPanel.mouseDownGridY] == 0 ; o--){

								/*	for(int o = myPanel.mouseDownGridX; o>=0 && myPanel.mines[o][myPanel.mouseDownGridY]==0 ; o--){
>>>>>>> branch 'master' of https://github.com/jose-otero12/Minesweeper-PA1.git
									myPanel.colorArray[o][myPanel.mouseDownGridY] = lightGray;
									myPanel.repaint();
								}
								for(int p = myPanel.mouseDownGridY; p>=0 && myPanel.mines[myPanel.mouseDownGridX][p]==0; p--){
									myPanel.colorArray[myPanel.mouseDownGridX][p] = lightGray;
									myPanel.repaint();
								}
								for(int q = myPanel.mouseDownGridX; q<=(MyPanel.getTotalRows()-1) && myPanel.mines[q][myPanel.mouseDownGridY]==0 ; q++){
									myPanel.colorArray[q][myPanel.mouseDownGridY] = lightGray;
									myPanel.repaint();
								}
								for(int r = myPanel.mouseDownGridY; r<=(MyPanel.getTotalColumns()-1) && myPanel.mines[myPanel.mouseDownGridX][r]==0; r++){
									myPanel.colorArray[myPanel.mouseDownGridX][r] = lightGray;
									myPanel.repaint();
								}*/


								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = lightGray;
								myPanel.repaint();

							}
						}
					}
				}
				myPanel.repaint();
				break;
			}
			case 3:		//Right mouse button
				//Do nothing


				break;
			default:    //Some other button (2 = Middle mouse button, etc.)
				//Do nothing
				break;
			}
		}
		
		public int getCounter()
		{
			return MyMouseAdapter.counter;
		}

	}