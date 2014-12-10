package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private PlayerDetailsPanel playerDet;
	private GameDetailsPanel gameDet;
	private BufferedImage map = null;
	
	public GamePanel(){
		setBackground(Color.WHITE);
		
		int oldH = 0, newH = 0;
		
		try {
			map = ImageIO.read(getClass().getClassLoader().getResourceAsStream("map.jpg"));
			oldH = map.getHeight();
			MainFrame.getInstance().setSize(new Dimension(1100, 600));
			MainFrame.getInstance().setLocationRelativeTo(null);
			map = resize(map);
			newH = map.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		playerDet = new PlayerDetailsPanel();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(0,0,0,0);
		add(playerDet, c);
		
		c.gridx = 1;
		c.gridy = 0;
		JLabel background = new JLabel(new ImageIcon(map));
		List<Route> routes = generateRoutes((double) oldH / newH);
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		for(Route r : routes){
			c.insets = getInsetForRoute(r.getId());
			add(r.getLabel(), c);
		}
		
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.CENTER;
		add(background, c);
		
		gameDet = new GameDetailsPanel();
		c.gridx = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(gameDet, c);
		
	}
	
	private BufferedImage resize(BufferedImage img) { 
		double ratio = (double) (MainFrame.getInstance().getHeight() - 50) / img.getHeight();
	    Image tmp = img.getScaledInstance((int) (img.getWidth() * ratio), (int) (img.getHeight() * ratio), Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage((int) (img.getWidth() * ratio), (int) (img.getHeight() * ratio), BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
	
	private List<Route> generateRoutes(double ratio){
		List<Route> routes = new ArrayList<Route>();
		for(shared.Route r : shared.Route.values()){
			routes.add(new Route(r.name(), ratio));
		}
		
		return routes;
	}
	
	private Insets getInsetForRoute(String key){
		Properties p = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream("route_positions.properties");
		try {
			p.load(is);
			String value = p.getProperty(key);
			
			String[] insets = value.split(",");
			
			return new Insets(Integer.parseInt(insets[0]),
							  Integer.parseInt(insets[1]),
							  Integer.parseInt(insets[2]),
							  Integer.parseInt(insets[3]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new Insets(0, 0, 0, 0);
	}
	
}