package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		myTickets.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO pass player ticket cards to constructor
				PlayerTicketsDialog ptd = new PlayerTicketsDialog();
				ptd.setVisible(true);
			}
		});
		add(myTickets);

		myTrains = new JButton("Vagonkártyáim");
		myTrains.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO pass player train cards to constructor
				PlayerTrainsDialog ptd = new PlayerTrainsDialog();
				ptd.setVisible(true);
			}
		});
		add(myTrains);
	}
	
	public JButton getMyTrains() {
		return myTrains;
	}

	public void setMyTrains(JButton myTrains) {
		this.myTrains = myTrains;
	}

	public JButton getMyTickets() {
		return myTickets;
	}

	public void setMyTickets(JButton myTickets) {
		this.myTickets = myTickets;
	}
}
