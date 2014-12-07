package view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerDetailsPanel extends JPanel {
	
	private JLabel myDetails;
	private JButton myTrains;
	private JButton myTickets;

	public PlayerDetailsPanel(){
		setLayout(new GridLayout(3, 1));
		setBackground(Color.WHITE);
		
		myDetails = new JLabel("Saját adataim");
		add(myDetails);
		
		myTickets = new JButton("Menetjegykártyáim");
		add(myTickets);

		myTrains = new JButton("Vagonkártyáim");
		add(myTrains);
	}
}
