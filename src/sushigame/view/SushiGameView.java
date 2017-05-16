package sushigame.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.Border;

import comp401.sushi.IngredientPortion;
import comp401.sushi.Plate;
import sushigame.model.Belt;
import sushigame.model.BeltEvent;
import sushigame.model.BeltObserver;
import sushigame.model.SushiGameModel;

public class SushiGameView extends JPanel implements ActionListener, BeltObserver {

	private PlayerChefView player_chef_ui;
	private List<RotationRequestListener> rotation_request_listeners;
	private JLabel controller_messages;

	private Belt belt;
	private ImageIcon genericSushi;
	private ImageIcon sadFace;

	ScoreboardWidget scoreboard;

	public SushiGameView(SushiGameModel game_model) {
		setLayout(new BorderLayout());

		scoreboard = new ScoreboardWidget(game_model);
		add(scoreboard, BorderLayout.WEST);

		player_chef_ui = new PlayerChefView(game_model.getBelt().getSize());
		player_chef_ui.setBackground(Color.PINK);
		add(player_chef_ui, BorderLayout.EAST);

		BeltView belt_view = new BeltView(game_model.getBelt());
		belt_view.setBorder(BorderFactory.createRaisedBevelBorder());
		belt_view.setBackground(Color.RED);

//		JSeparator space = new JSeparator();
//		space.setPreferredSize(new Dimension(500,20));
//		add(space);
		
		belt = belt_view.getBelt();
		genericSushi = belt_view.getSushiIcon();
		sadFace = belt_view.getSadFace();

		belt_view.registerButtonListener(this);
		add(belt_view, BorderLayout.CENTER);

		JPanel bottom_panel = new JPanel();
		bottom_panel.setLayout(new BorderLayout());
		bottom_panel.setBackground(Color.PINK);

		JButton rotate_button = new JButton("Rotate");
		rotate_button.setActionCommand("rotate");
		rotate_button.addActionListener(this);

		bottom_panel.add(rotate_button, BorderLayout.WEST);

		controller_messages = new JLabel("Controller messages.");
		bottom_panel.add(controller_messages, BorderLayout.CENTER);


		add(bottom_panel, BorderLayout.SOUTH);

		rotation_request_listeners = new ArrayList<RotationRequestListener>();

		game_model.getBelt().registerBeltObserver(this);
	}

	public void registerPlayerChefListener(ChefViewListener cl) {
		player_chef_ui.registerChefListener(cl);
	}

	public void registerRotationRequestListener(RotationRequestListener rrl) {
		rotation_request_listeners.add(rrl);
	}


	String plateColor;
	String chefName;
	Plate plate;
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("rotate")) {
			for (RotationRequestListener rrl : rotation_request_listeners) {
				rrl.handleRotationRequest();
			}
		}
		for(int i=0;i<belt.getSize();i++){
			if (e.getActionCommand().equals("click_"+i)){
				plate = belt.getPlateAtPosition(i);
				if(plate == null){
					JOptionPane.showMessageDialog(this,
							"No Plate at position "+i,
							"No Plate", JOptionPane.DEFAULT_OPTION,sadFace);
				} else {
					switch(plate.getColor()){
					case RED: 
						plateColor = "red";
						break;
					case BLUE:
						plateColor = "blue";
						break;
					case GREEN:
						plateColor = "green";
						break;
					case GOLD:
						plateColor = "gold";
						break;
					}
					chefName = plate.getChef().getName();
					if(plate.getContents().getName().contains("Roll")){
						JOptionPane.showMessageDialog(this,
								"Plate Color: "+plateColor+
								"\nAge of Plate: "+belt.getAgeOfPlateAtPosition(i)+
								"\nType of Sushi: "+plate.getContents().getName()+
								"\nIngredients: "+listRollIngredients(plate),
								chefName, 
								JOptionPane.DEFAULT_OPTION, genericSushi);
					}else{
						JOptionPane.showMessageDialog(this,
								"Plate Color: "+plateColor+
								"\nAge of Plate: "+belt.getAgeOfPlateAtPosition(i)+
								"\nType of Sushi: "+plate.getContents().getName()
								,
								chefName, 
								JOptionPane.DEFAULT_OPTION, genericSushi);
					}
				}
			}
		}

	}


	public String listRollIngredients(Plate plate){
		String list = "";
		IngredientPortion[] roll_ing = plate.getContents().getIngredients();
		for(int i=0;i<roll_ing.length;i++){
			String ing = roll_ing[i].getName();
			Double amt = ((int) (roll_ing[i].getAmount() * 100.0 + 0.5))/100.0;
			String amt_string = Double.toString(amt)+" oz";
			list+="\n"+amt_string+" of "+ing;
		}
		return list;
	}

	public void setControllerMessage(String message) {
		controller_messages.setText(message);
	}

	@Override
	public void handleBeltEvent(BeltEvent e) {
		if (e.getType() == BeltEvent.EventType.ROTATE) {
			controller_messages.setText("");
		}
	}

	public void refreshScoreboard() {
		scoreboard.refresh();
	}
}
