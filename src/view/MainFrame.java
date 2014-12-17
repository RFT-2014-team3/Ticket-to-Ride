package view;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import shared.PlayerColor;

public class MainFrame extends JFrame {
	
	JPanel currentPanel;
	
	private static final MainFrame frame = new MainFrame();

	private MainFrame(){
		setMinimumSize(new Dimension(600, 600));
		setSize(new Dimension(600,600));
		setTitle("Ticket to Ride");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		currentPanel = new MainMenu();
		this.getContentPane().add(currentPanel);
		
		setVisible(true);
	}
	
	public static MainFrame getInstance(){
		return frame;
	}
	
	public void setPanel(JPanel panel){
		currentPanel = panel;
		Container c = getContentPane();
		c.removeAll();
		c.add(currentPanel);
		setContentPane(c);
	}
	
	public void showTrainCard(String name){
		if(currentPanel instanceof GamePanel){
			((GamePanel) currentPanel).showTrainCard(name);
		}
	}
	
	public void updateTablePositions(PlayerColor color, int position){
		if(currentPanel instanceof GamePanel){
			((GamePanel) currentPanel).updateTablePosition(color, position);
		}
	}
}
