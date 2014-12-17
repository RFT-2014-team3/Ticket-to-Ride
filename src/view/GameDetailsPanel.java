package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logicmodule.Controller;
import logicmodule.GUIHandler;
import shared.TrainColor;

public class GameDetailsPanel extends JPanel {

	private JLabel gameDetails;
	private JLabel ticketsDeck;
	private JLabel trainsDeck;
	private List<JLabel> upfaceTrains = new ArrayList<>();
	private GUIHandler con = Controller.getInstance();
	
	public GameDetailsPanel(){
		setLayout(new GridLayout(9, 1));
		setBackground(Color.WHITE);
		
		gameDetails = new JLabel("Játék adatok");
		
		add(gameDetails);
		
		ticketsDeck = new JLabel(new ImageIcon(resize(loadImage("ticketcard"), 0.25)));
		
		ticketsDeck.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				ticketsDeck.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				ticketsDeck.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				con.drawTicketCards();
				((GamePanel)MainFrame.getInstance().currentPanel).waitForTickets(false);
			}
		});
		
		trainsDeck = new JLabel(new ImageIcon(resize(loadImage("traincard"), 0.25)));
		trainsDeck.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				trainsDeck.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				trainsDeck.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				con.drawTrainCard();
			}
		});
		
		add(ticketsDeck);
		add(trainsDeck);
		
		final JLabel phCard = new JLabel(new ImageIcon(resize(loadImage("placeholder"), 0.25)));
		add(phCard);
		for(TrainColor tc : con.getUpfaceTrainCards()){
			if(tc == null){
				upfaceTrains.add(phCard);
				add(phCard);
				continue;
			}
			double ratio = 0.35;
			if(tc == TrainColor.GREY)
				ratio *= 0.51;
			final JLabel card = new JLabel(new ImageIcon(resize(loadImage("trains/" + tc), ratio)));
			card.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					card.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					con.drawTrainCard(upfaceTrains.indexOf(card));
				}
			});
			upfaceTrains.add(card);
			add(card);
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
