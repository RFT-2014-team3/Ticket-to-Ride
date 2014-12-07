package view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameDetailsPanel extends JPanel {

	private JLabel gameDetails;
	
	public GameDetailsPanel(){
		setLayout(new GridLayout(1, 1));
		setBackground(Color.WHITE);
		
		gameDetails = new JLabel("Játék adatok");
		
		add(gameDetails);
	}
}
