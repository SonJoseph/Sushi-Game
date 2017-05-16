package sushigame.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp401.sushi.Plate;
import sushigame.model.Belt;
import sushigame.model.BeltEvent;
import sushigame.model.BeltObserver;
import sushigame.model.Chef;

public class BeltView extends JPanel implements BeltObserver{

	private Belt belt;
	private JButton[] buttons;
	private ImageIcon genericSushi;
	private ImageIcon sadFace;
	
	public BeltView(Belt b) {
		this.belt = b;
		belt.registerBeltObserver(this);
		setLayout(new GridLayout(0,1));
		buttons = new JButton[belt.getSize()];
		Image findSushi;
		Image findSad;
		try {
			findSushi = ImageIO.read(new FileInputStream("Images/salada-maki-icon.png"));
			findSushi = findSushi.getScaledInstance(20,20,Image.SCALE_DEFAULT);
			genericSushi = new ImageIcon(findSushi);
			
			findSad = ImageIO.read(new FileInputStream("Images/sad-face-icon.png"));
			findSad = findSad.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
			sadFace = new ImageIcon(findSad);
		} catch (IOException e) {
		System.out.println("Can't find image!");
		}
		for (int i = 0; i < belt.getSize(); i++) {
			if (belt.getPlateAtPosition(i)!=null){
				JButton sushiPlate = new JButton(belt.getPlateAtPosition(i).getContents().getName(), genericSushi);
				sushiPlate.setHorizontalAlignment(JLabel.CENTER);
				add(sushiPlate);
				buttons[i] = sushiPlate;
			} else {
				buttons[i] = new JButton("No Plate", sadFace);
				buttons[i].setBackground(Color.WHITE);
				buttons[i].setPreferredSize(new Dimension(500,30));
				buttons[i].setHorizontalAlignment(JLabel.CENTER);
				add(buttons[i]);
			}
		}
		for (int i =0; i<buttons.length;i++){
			buttons[i].setActionCommand("click_"+i);
		}
		refresh();
	}
	
	@Override
	public void handleBeltEvent(BeltEvent e) {	
		refresh();
	}
	
	String plateColor;
	private void refresh() {
		for(int i=0;i<belt.getSize();i++){
			buttons[i].setFont(new Font("Arial", Font.BOLD, 14));
			if(belt.getPlateAtPosition(i) == null){
				buttons[i].setForeground(Color.black);
				buttons[i].setText("No Plate");
				buttons[i].setIcon(sadFace);
			} else {
				switch(belt.getPlateAtPosition(i).getColor()){
				case RED: 
					plateColor = "red";
					buttons[i].setForeground(Color.RED);
					break;
				case BLUE:
					plateColor = "blue";
					buttons[i].setForeground(Color.BLUE);
					break;
				case GREEN:
					plateColor = "green";
					buttons[i].setForeground(Color.GREEN);
					break;
				case GOLD:
					plateColor = "gold";
					buttons[i].setForeground(Color.getHSBColor(73, 64, 58));
					break;
				}
				String chefName = belt.getPlateAtPosition(i).getChef().getName();
				buttons[i].setText("Try "+chefName+"'s sushi");
				buttons[i].setIcon(genericSushi);
			}
		}
	}

	public void registerButtonListener(ActionListener l){
		for(int i =0; i<buttons.length;i++){
		buttons[i].addActionListener(l);
		}
	}
	
	public Belt getBelt(){
		return belt;
	}
	
	public ImageIcon getSushiIcon(){
		return genericSushi;
	}
	
	public ImageIcon getSadFace(){
		return sadFace;
	}
	
}
