import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.util.Random;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class MyMouseAdapter extends MouseAdapter {
	//private Random generator = new Random();
	private static int counter = 0;
	private boolean gameOver = false;
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

			if ((x >= restartButton.getX() && x <= restartButton.getX() + restartButton.getWidth()) && 
					(y >= restartButton.getY() && y <= restartButton.getY() + restartButton.getHeight())) {
				gameOver=false;
				myPanel.reset();
				//myPanel = new MyPanel();
				//myPanel.repaint();
			}


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
			myPanel.redFlag();		

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
			if(!this.gameOver){
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
			if ((gridX == -1) || (gridY == -1)) {
				//Had pressed outside

				//	myPanel.paintMinesHack();

			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
					
				} 
				else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} 
					else {
						//Released the mouse button on the same cell where it was pressed
						if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] != Color.RED){
							myPanel.clickAT(gridX, gridY);
							if(!myPanel.isFinished()) {
								if (myPanel.mines[gridX][gridY] == 1) {
									myPanel.paintMines();
									gameOver = true;
								}
								else {

									int adjacentCount = myPanel.getAdjacentMines(gridX, gridY);
									if(adjacentCount==0){

										myPanel.dominoEffect(myPanel.mouseDownGridX, myPanel.mouseDownGridY);
										myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.LIGHT_GRAY;
										myPanel.repaint();

										counter = 0;
									}
									else {
										myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.LIGHT_GRAY;
										myPanel.repaint();
										JLabel label = myPanel.getLabelAt(gridX, gridY);
										
										label.setVisible(true);
									}
								}
							}

							else {
								myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.LIGHT_GRAY;
								myPanel.repaint();
								JLabel label = myPanel.getLabelAt(gridX, gridY);
								label.setVisible(true);
								myPanel.endGameResult(true);
								
							}

						}
					}


					myPanel.repaint();
					break;
				}
			}

			}
		case 3:		//Right mouse button
			// do nothing
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