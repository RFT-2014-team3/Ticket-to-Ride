package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;

import shared.TrainColor;

public class PlayerTrainsDialog extends JDialog {
	
	

	public PlayerTrainsDialog(List<Integer> trainsList){
		setBackground(Color.WHITE);
		setMinimumSize(new Dimension(600, 400));
		setLayout(new GridLayout(2, 9, 10, 5));
		setLocationRelativeTo(MainFrame.getInstance());
		
		for(int i = 0; i < trainsList.size(); i++){
			JLabel img = new JLabel(TrainColor.values()[i].toString());
			add(img);
		}
		
		for(int i = 0; i < trainsList.size(); i++){
			JLabel count = new JLabel(String.valueOf(trainsList.get(i)));
			add(count);
		}
		
	}
}
