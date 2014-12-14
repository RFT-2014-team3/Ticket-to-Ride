package view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WaitingScreen extends JPanel {

	private final JLabel waitingLabel = new JLabel();
	private final JLabel mainLabel = new JLabel();
	private final JLabel nofWaiting = new JLabel();
	private final JButton start = new JButton();
	
	public WaitingScreen(boolean isHost){
		if(isHost){
			setHostWaitingScreen();
		} else{
			setClientWaitingScreen();
		}
	}
	
	private void setHostWaitingScreen(){
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		
		GridBagConstraints c = new GridBagConstraints();
		
		mainLabel.setText("Várakozó játékosok száma: ");
		
		nofWaiting.setText("1");
		
		start.setText("Játék indítása");
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(logicmodule.Controller.getInstance().startGame())
					MainFrame.getInstance().setPanel(new GamePanel());
			}
		});
		
		c.gridx = 0;
		c.gridy = 0;
		add(mainLabel, c);
		
		c.gridx = 1;
		c.gridy = 0;
		add(nofWaiting, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		add(start, c);
	}
	
	private void setClientWaitingScreen() {
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		
		GridBagConstraints c = new GridBagConstraints();
		
		waitingLabel.setText("Várakozás játékosokra...");
		
		mainLabel.setText("Várakozó játékosok száma: ");
		
		nofWaiting.setText("1");

		c.gridx = 0;
		c.gridy = 0;
		add(waitingLabel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(mainLabel, c);
		
		c.gridx = 1;
		c.gridy = 1;
		add(nofWaiting, c);
		
	}
}
