package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartNewGame extends JPanel {

	private final JLabel mainLabel;
	private final JLabel ipLabel;
	private final JButton proceed;

	public StartNewGame(){
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		
		GridBagConstraints c = new GridBagConstraints();
		
		mainLabel = new JLabel("Az IP címed:");
		mainLabel.setFont(new Font(mainLabel.getFont().getName(), Font.PLAIN, 20));
		String ip = logicmodule.Controller.getInstance().startNewServer();
		ipLabel = new JLabel(ip);
		ipLabel.setFont(new Font(ipLabel.getFont().getName(), Font.BOLD, 30));
		
		proceed = new JButton("Tovább");
		
		proceed.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().setPanel(new WaitingScreen(true));
			}
		});
		
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 10;
		add(mainLabel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(ipLabel, c);
		
		c.gridx = 0;
		c.gridy = 2;
		add(proceed, c);
		
	}
}
