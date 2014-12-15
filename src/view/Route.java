package view;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import logicmodule.Controller;

public class Route {

	private String id;
	private BufferedImage logo;
	private BufferedImage icon;
	private JLabel label;

	public Route(String name, double ratio){
		this.id = name;
		this.logo = loadImage(name);
		this.icon = resize(getBufImg(this.logo), ratio);
		label = new JLabel(new ImageIcon(this.icon));
		
		label.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if(clickAlphaValue(icon, e.getX(), e.getY()) != 0){
					label.setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else{
					label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
		});
		
		label.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(clickAlphaValue(icon, e.getX(), e.getY()) != 0){
					Controller.getInstance().claimRoute(shared.Route.valueOf(id));
				} 
			}
		});
	}
	
	private BufferedImage loadImage(String name){
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getClassLoader().getResourceAsStream("routes/" + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return img;
	}
	
	private int clickAlphaValue(BufferedImage bufImg, int posX, int posY){
	    int alpha;

	    alpha = (bufImg.getRGB(posX, posY) >>24) & 0x000000FF;

	    return alpha;
	}
	
	public BufferedImage getBufImg(BufferedImage image){
	    BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = newImg.createGraphics();
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

	    g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	    g2d.dispose();
	
	    return newImg;
	}
	
	private BufferedImage resize(BufferedImage img, double ratio) { 
	    Image tmp = img.getScaledInstance((int) (img.getWidth() / ratio), (int) (img.getHeight() / ratio), Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage((int) (img.getWidth() / ratio), (int) (img.getHeight() / ratio), BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	   
}
