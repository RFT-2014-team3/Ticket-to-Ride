package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class PlayerTicketsDialog extends JDialog {

	public PlayerTicketsDialog(){
		setBackground(Color.WHITE);
		setMinimumSize(new Dimension(100, 100));
		setLocationRelativeTo(MainFrame.getInstance());
		JLabel label = new JLabel("testmessage");
		add(label);
	}
}
