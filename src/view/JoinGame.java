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
import javax.swing.JTextField;

public class JoinGame extends JPanel {

	private final JLabel mainLabel;
	private final JTextField ipInput;
	private final JButton join;

	public JoinGame(){
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		
		GridBagConstraints c = new GridBagConstraints();
		
		mainLabel = new JLabel("IP cím:");
		mainLabel.setFont(new Font(mainLabel.getFont().getName(), Font.PLAIN, 20));
		
		ipInput = new JTextField();
		ipInput.setColumns(20);
		
		join = new JButton("Csatlakozás");
		
		join.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().setPanel(new WaitingScreen(false));
			}
		});
		
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 10;
		add(mainLabel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(ipInput, c);
		
		c.gridx = 0;
		c.gridy = 2;
		add(join, c);
		
	}
}
