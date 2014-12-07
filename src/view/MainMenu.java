package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JPanel {

	private final JButton newGame;
	private final JButton joinGame;
	
	public MainMenu(){
		newGame = new JButton("Új játék");
		joinGame = new JButton("Csatlakozás játékhoz");
		
		newGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().setPanel(new StartNewGame());
			}
		});
		
		joinGame.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().setPanel(new JoinGame());
			}
			
		});
		
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		
		JPanel menu = new JPanel();
		menu.setBackground(Color.WHITE);
		menu.setLayout(new GridLayout(2,1,0,30));
		menu.setBorder(new EmptyBorder(20,200,0,200));
		
		menu.add(newGame);
		menu.add(joinGame);
		
		add(menu, BorderLayout.CENTER);
	}
}
