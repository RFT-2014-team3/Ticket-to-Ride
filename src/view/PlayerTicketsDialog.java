package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import shared.TicketCard;

public class PlayerTicketsDialog extends JDialog {

	public PlayerTicketsDialog(List<TicketCard> ticketCards){
		setBackground(Color.WHITE);
		setLayout(new GridLayout(5, 5, 5, 5));
		setMinimumSize(new Dimension(600, 400));
		setLocationRelativeTo(MainFrame.getInstance());
		
		for(TicketCard tc : ticketCards){
			BufferedImage img = resize(loadImage("ticketcards/" + tc), 0.15);
			JLabel label = new JLabel(new ImageIcon(img));
			add(label);
		}
		
	}
	
	private BufferedImage loadImage(String name){
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return img;
	}
	
	private BufferedImage resize(BufferedImage img, double ratio) { 
	    Image tmp = img.getScaledInstance((int) (img.getWidth() * ratio), (int) (img.getHeight() * ratio), Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage((int) (img.getWidth() * ratio), (int) (img.getHeight() * ratio), BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
}
