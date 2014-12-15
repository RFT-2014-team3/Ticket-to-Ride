package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logicmodule.Controller;

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
				PlayerTicketsDialog ptd = new PlayerTicketsDialog(Controller.getInstance().getMyTicketCards());
				ptd.setVisible(true);
			}
		});
		add(myTickets);

		myTrains = new JButton("Vagonkártyáim");
		myTrains.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PlayerTrainsDialog ptd = new PlayerTrainsDialog(Controller.getInstance().getMyTrainCards());
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
