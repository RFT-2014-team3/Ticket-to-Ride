package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logicmodule.Controller;
import logicmodule.GUIHandler;

public class PlayerDetailsPanel extends JPanel {
	
	private JLabel myDetails;
	private JButton myTrains;
	private JButton myTickets;
	private JLabel myTurnLabel;
	private JLabel trainsLeft;
	private GUIHandler con = Controller.getInstance();

	public PlayerDetailsPanel(){
		setLayout(new GridLayout(5, 1));
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
		
		final String prefix = "<html><font color='" + con.getPlayerColors().get(con.getMyID() - 1).name().toLowerCase() + "'>";
		final String postfix = "</font></html>";
		myTurnLabel = new JLabel(prefix + (con.isMyTurn() ? "Te következel" : "Várakozás a másik játékosra") + postfix);
		add(myTurnLabel);
		
		trainsLeft = new JLabel("Vasúti kocsik: 45");
		add(trainsLeft);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				myTurnLabel.setText(prefix + (con.isMyTurn() ? "Te következel" : "Várakozás a másik játékosra") + postfix);
				trainsLeft.setText("Vasúti kocsik: " + Controller.getInstance().getPlayerById(con.getMyID() - 1).getRemainingTrainsCount());
			}
		}, 0, 100);
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
