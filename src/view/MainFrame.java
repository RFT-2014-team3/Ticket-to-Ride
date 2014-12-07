package view;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	
	private JPanel currentPanel;
	
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
}
