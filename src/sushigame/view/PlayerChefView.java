package sushigame.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import comp401.sushi.AvocadoPortion;
import comp401.sushi.CrabPortion;
import comp401.sushi.EelPortion;
import comp401.sushi.IngredientPortion;
import comp401.sushi.Nigiri;
import comp401.sushi.Nigiri.NigiriType;
import comp401.sushi.Plate;
import comp401.sushi.RedPlate;
import comp401.sushi.RicePortion;
import comp401.sushi.Roll;
import comp401.sushi.SalmonPortion;
import comp401.sushi.Sashimi;
import comp401.sushi.Sashimi.SashimiType;
import comp401.sushi.SeaweedPortion;
import comp401.sushi.ShrimpPortion;
import comp401.sushi.Sushi;
import comp401.sushi.Tuna;
import comp401.sushi.TunaPortion;

//This code NEEEEEDS to be cleaned up (aka seperate classes for each panel)
public class PlayerChefView extends JPanel implements ActionListener, ChangeListener {

	private List<ChefViewListener> listeners;
	private int belt_size;

	private JCheckBox[] ingsForRoll;
	private JSlider[] amtOfIngs;
	private JRadioButton[] ings;
	private JRadioButton[] sushiTypes;
	private JRadioButton[] plates;

	private ButtonGroup sushiTypeGroup;
	private ButtonGroup ingGroup;
	private ButtonGroup plateGroup;

	JButton send;

	private JSlider position_slider;
	private JSlider goldPriceSlider;

	private JSlider tunaRollSlider;
	private JSlider salmonRollSlider;
	private JSlider eelRollSlider;
	private JSlider crabRollSlider;
	private JSlider shrimpRollSlider;
	private JSlider avocadoRollSlider;
	private JSlider riceRollSlider;
	private JSlider seaweedRollSlider;

	private int at_position;
	private double goldPrice;

	private double tunaRollAmt;
	private double salmonRollAmt;
	private double eelRollAmt;
	private double crabRollAmt;
	private double shrimpRollAmt;
	private double avocadoRollAmt;
	private double riceRollAmt;
	private double seaweedRollAmt;

	private List<IngredientPortion> roll;
	private int num_ing;

	public PlayerChefView(int belt_size) {
		this.belt_size = belt_size;
		listeners = new ArrayList<ChefViewListener>();

		setLayout(new BorderLayout());

		position_slider = new JSlider(0,20);
		goldPriceSlider = new JSlider(500,1000);
		tunaRollSlider = new JSlider(0,150);
		salmonRollSlider = new JSlider(0,150);
		eelRollSlider = new JSlider(0,150);
		crabRollSlider = new JSlider(0,150);
		shrimpRollSlider = new JSlider(0,150);
		avocadoRollSlider = new JSlider(0,150);
		riceRollSlider = new JSlider(0,150);
		seaweedRollSlider = new JSlider(0,150);

		at_position = 0;
		goldPrice = 0;
		tunaRollAmt = 0.0;
		salmonRollAmt = 0.0;
		eelRollAmt = 0.0;
		crabRollAmt = 0.0;
		shrimpRollAmt = 0.0;
		avocadoRollAmt = 0.0;
		riceRollAmt = 0.0;
		seaweedRollAmt = 0.0;

		roll = new ArrayList<IngredientPortion>();

		JPanel chooseType = new JPanel();
		chooseType.setLayout(new BoxLayout(chooseType, BoxLayout.Y_AXIS));
		chooseType.setBackground(Color.PINK);
		chooseType.setBorder(BorderFactory.createRaisedBevelBorder());
		JLabel chooseTypeTitle = new JLabel("Choose Sushi Type");
		chooseType.add(chooseTypeTitle);
		add(chooseType, BorderLayout.PAGE_START);

		sushiTypeGroup = new ButtonGroup();
		JRadioButton sashimi_button = new JRadioButton("Sashimi");
		sashimi_button.setActionCommand("Sashimi");
		JRadioButton nigiri_button = new JRadioButton("Nigiri");
		nigiri_button.setActionCommand("Nigiri");
		JRadioButton roll_button = new JRadioButton("Roll");
		roll_button.setActionCommand("Roll");
		sushiTypes = new JRadioButton[]{sashimi_button, nigiri_button, roll_button};
		for(int i=0;i<sushiTypes.length;i++){
			sushiTypes[i].setBackground(Color.PINK);
			sushiTypes[i].addActionListener(this);
			sushiTypeGroup.add(sushiTypes[i]);
			chooseType.add(sushiTypes[i]);
		}

		JPanel chooseIngs = new JPanel();
		chooseIngs.setLayout(new BoxLayout(chooseIngs, BoxLayout.Y_AXIS));
		chooseIngs.setBorder(BorderFactory.createRaisedBevelBorder());
		chooseIngs.setBackground(Color.PINK);
		JLabel chooseIngsTitle = new JLabel("Choose Ingredients for Sashimi or Nigiri");
		chooseIngs.add(chooseIngsTitle);
		add(chooseIngs, BorderLayout.LINE_START);

		ingGroup = new ButtonGroup();
		JRadioButton tuna = new JRadioButton("Tuna");
		tuna.setActionCommand("Tuna");
		JRadioButton salmon = new JRadioButton("Salmon");
		salmon.setActionCommand("Salmon");
		JRadioButton eel = new JRadioButton("Eel");
		eel.setActionCommand("Eel");
		JRadioButton crab = new JRadioButton("Crab");
		crab.setActionCommand("Crab");
		JRadioButton shrimp = new JRadioButton("Shrimp");
		shrimp.setActionCommand("Shrimp");
		ings = new JRadioButton[]{tuna, salmon, eel, crab, shrimp};
		for(int i=0;i<ings.length;i++){
			ings[i].setBackground(Color.PINK);
			ings[i].addActionListener(this);
			ings[i].setEnabled(false);
			ingGroup.add(ings[i]);
			chooseIngs.add(ings[i]);
		}


		JPanel chooseRollIngs = new JPanel();
		chooseRollIngs.setLayout(new BoxLayout(chooseRollIngs, BoxLayout.Y_AXIS));
		chooseRollIngs.setBackground(Color.PINK);
		chooseRollIngs.setBorder(BorderFactory.createRaisedBevelBorder());
		JLabel chooseRollIngsTitle = new JLabel("1. Choose Amount (oz) of Ingredients for Roll");
		chooseRollIngs.add(chooseRollIngsTitle);
		JLabel blank = new JLabel("2. Chose desired Ingredients for Roll");
		chooseRollIngs.add(blank);
		add(chooseRollIngs, BorderLayout.CENTER);

		JCheckBox tunaRoll = new JCheckBox("Tuna");
		tunaRoll.setActionCommand("TunaRoll");
		JCheckBox salmonRoll = new JCheckBox("Salmon");
		salmonRoll.setActionCommand("SalmonRoll");
		JCheckBox eelRoll = new JCheckBox("Eel");
		eelRoll.setActionCommand("EelRoll");
		JCheckBox crabRoll = new JCheckBox("Crab");
		crabRoll.setActionCommand("CrabRoll");
		JCheckBox shrimpRoll = new JCheckBox("Shrimp");
		shrimpRoll.setActionCommand("ShrimpRoll");
		JCheckBox avocado = new JCheckBox("Avocado");
		avocado.setActionCommand("Avocado");
		JCheckBox rice = new JCheckBox("Rice");
		rice.setActionCommand("Rice");
		JCheckBox seaweed = new JCheckBox("Seaweed");
		seaweed.setActionCommand("Seaweed");

		ingsForRoll = new JCheckBox[]{tunaRoll, salmonRoll, eelRoll, crabRoll, shrimpRoll, avocado, rice, seaweed};
		amtOfIngs = new JSlider[]{tunaRollSlider, salmonRollSlider, eelRollSlider, crabRollSlider, shrimpRollSlider, avocadoRollSlider, riceRollSlider, seaweedRollSlider};

		for(int i=0; i<ingsForRoll.length; i++){
			ingsForRoll[i].setBackground(Color.PINK);
			ingsForRoll[i].addActionListener(this);
			ingsForRoll[i].setEnabled(false);
			chooseRollIngs.add(ingsForRoll[i]);

			amtOfIngs[i].setBackground(Color.pink);
			goldPriceSlider.setMajorTickSpacing(50);
			goldPriceSlider.setMinorTickSpacing(25);

			Hashtable labels= new Hashtable();
			labels.put(new Integer(0), new JLabel("0.00"));
			labels.put(new Integer(50), new JLabel(".50"));
			labels.put(new Integer(100), new JLabel("1.00"));
			labels.put(new Integer(150), new JLabel("1.50"));
			amtOfIngs[i].setLabelTable(labels);

			amtOfIngs[i].setPaintTicks(true);
			amtOfIngs[i].setPaintLabels(true);
			amtOfIngs[i].setValue(0);
			amtOfIngs[i].addChangeListener(this);
			amtOfIngs[i].setEnabled(false);
			chooseRollIngs.add(amtOfIngs[i]);
		}

		plateGroup = new ButtonGroup();
		JPanel choosePlateColor = new JPanel();
		choosePlateColor.setLayout(new BoxLayout(choosePlateColor,BoxLayout.Y_AXIS));
		choosePlateColor.setBackground(Color.PINK);
		choosePlateColor.setBorder(BorderFactory.createRaisedBevelBorder());
		JLabel choosePlateColorTitle = new JLabel("Choose Plate Color");
		choosePlateColor.add(choosePlateColorTitle);
		add(choosePlateColor, BorderLayout.LINE_END);

		JRadioButton red = new JRadioButton("Red");
		red.setActionCommand("Red");
		JRadioButton blue = new JRadioButton("Blue");
		blue.setActionCommand("Blue");
		JRadioButton green = new JRadioButton("Green");
		green.setActionCommand("Green");
		JRadioButton gold = new JRadioButton("Gold");
		gold.setActionCommand("Gold");
		plates = new JRadioButton[]{red,blue,green,gold};
		for(int i=0; i<plates.length;i++){
			plates[i].setEnabled(false);
			plates[i].addActionListener(this);
			plates[i].setBackground(Color.PINK);
			plateGroup.add(plates[i]);
			choosePlateColor.add(plates[i]);
		}

		JLabel title = new JLabel("Choose price of gold plate");
		title.setBackground(Color.PINK);

		choosePlateColor.add(title);
		goldPriceSlider.setBackground(Color.pink);
		goldPriceSlider.addChangeListener(this);
		goldPriceSlider.setMajorTickSpacing(100);
		goldPriceSlider.setMinorTickSpacing(25);
		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(500), new JLabel("5.00"));
		labelTable.put(new Integer(600), new JLabel("6.00"));
		labelTable.put(new Integer(700), new JLabel("7.00"));
		labelTable.put(new Integer(800), new JLabel("8.00"));
		labelTable.put(new Integer(900), new JLabel("9.00"));
		labelTable.put(new Integer(1000), new JLabel("10.00"));
		goldPriceSlider.setLabelTable(labelTable);
		goldPriceSlider.setPaintTicks(true);
		goldPriceSlider.setPaintLabels(true);
		goldPriceSlider.setValue(500);
		goldPriceSlider.setEnabled(false);
		choosePlateColor.add(goldPriceSlider);

		JPanel conclude = new JPanel();
		conclude.setLayout(new BorderLayout());
		conclude.setBackground(Color.PINK);
		JLabel question = new JLabel("Where do you wish to put your plate?");
		conclude.add(question, BorderLayout.PAGE_START);

		position_slider.setBackground(Color.pink);
		position_slider.addChangeListener(this);
		position_slider.setMajorTickSpacing(1);
		position_slider.setPaintTicks(true);
		position_slider.setPaintLabels(true);
		position_slider.setValue(0);
		conclude.add(position_slider);
		conclude.add(position_slider, BorderLayout.CENTER);
		send = new JButton("Place Plate on Belt?");
		send.setEnabled(false);
		send.addActionListener(this);
		send.setActionCommand("Send");
		conclude.add(send, BorderLayout.PAGE_END);
		add(conclude,BorderLayout.PAGE_END);
	}

	public void registerChefListener(ChefViewListener cl) {
		listeners.add(cl);
	}

	private void makeRedPlateRequest(Sushi plate_sushi, int plate_position) {
		for (ChefViewListener l : listeners) {
			l.handleRedPlateRequest(plate_sushi, plate_position);
		}
	}

	private void makeGreenPlateRequest(Sushi plate_sushi, int plate_position) {
		for (ChefViewListener l : listeners) {
			l.handleGreenPlateRequest(plate_sushi, plate_position);
		}
	}

	private void makeBluePlateRequest(Sushi plate_sushi, int plate_position) {
		for (ChefViewListener l : listeners) {
			l.handleBluePlateRequest(plate_sushi, plate_position);
		}
	}

	private void makeGoldPlateRequest(Sushi plate_sushi, int plate_position, double price) {
		for (ChefViewListener l : listeners) {
			l.handleGoldPlateRequest(plate_sushi, plate_position, price);
		}
	}

	private String sushiType;
	private SashimiType sashimiType;
	private NigiriType nigiriType;
	private Plate.Color plateColor;
	private Sushi sushi;
	private IngredientPortion[] finalRoll;

	@Override
	public void actionPerformed(ActionEvent e) {
		ct = 0;
		switch(e.getActionCommand()){
		case "Sashimi":
			sushiType = "Sashimi";
			ings(true);
			ingsRoll(false);
			amtRoll(false);
			break;
		case "Nigiri":
			sushiType = "Nigiri";
			ings(true);
			ingsRoll(false);
			amtRoll(false);
			break;
		case "Roll":
			sushiType = "Roll";
			ings(false);
			amtRoll(true);
			break;
		case "Red":
			plateColor = Plate.Color.RED;
			send.setEnabled(true);
			break;
		case "Blue":
			plateColor = Plate.Color.BLUE;
			send.setEnabled(true);
			break;
		case "Green":
			plateColor = Plate.Color.GREEN;
			send.setEnabled(true);
			break;
		case "Gold":
			send.setEnabled(true);
			plateColor = Plate.Color.GOLD;
			goldPriceSlider.setEnabled(true);
			break;
			//need to account for if send button is pressed before everything
		case "Send":
			if(sushiType.equals("Roll")){
				finalRoll = new IngredientPortion[num_ing];
				int j = 0;
				for (int i=0;i<roll.size();i++){
					if(roll.get(i) == null){
						continue;
					}else{
						finalRoll[j] = roll.get(i);
						j++;
					}
				}
				sushi = new Roll("My Roll", finalRoll);
			}
			switch(plateColor){
			case RED:
				makeRedPlateRequest(sushi, at_position);
				break;
			case BLUE:
				makeBluePlateRequest(sushi, at_position);
				break;
			case GREEN:
				makeGreenPlateRequest(sushi, at_position);
				break;
			case GOLD:
				makeGoldPlateRequest(sushi, at_position, goldPrice);
				break;
			}
			roll = new ArrayList<IngredientPortion>();
			refresh();
			break;
		}

		switch(sushiType){
		case "Sashimi":
			switch(e.getActionCommand()){
			case "Tuna":
				sashimiType = SashimiType.TUNA;
				sushi = new Sashimi(sashimiType);
				System.out.println("Added!");
				break;
			case "Salmon":
				sashimiType = SashimiType.SALMON;
				sushi = new Sashimi(sashimiType);
				break;
			case "Eel":
				sashimiType = SashimiType.EEL;
				sushi = new Sashimi(sashimiType);
				break;
			case "Crab":
				sashimiType = SashimiType.CRAB;
				sushi = new Sashimi(sashimiType);
				break;
			case "Shrimp":
				sashimiType = SashimiType.SHRIMP;
				sushi = new Sashimi(sashimiType);
				break;
			}
			break;
		case "Nigiri":
			switch(e.getActionCommand()){
			case "Tuna":
				nigiriType = NigiriType.TUNA;
				sushi = new Nigiri(nigiriType);
				break;
			case "Salmon":
				nigiriType = NigiriType.SALMON;
				sushi = new Nigiri(nigiriType);
				break;
			case "Eel":
				nigiriType = NigiriType.EEL;
				sushi = new Nigiri(nigiriType);
				break;
			case "Crab":
				nigiriType = NigiriType.CRAB;
				sushi = new Nigiri(nigiriType);
				break;
			case "Shrimp":
				nigiriType = NigiriType.SHRIMP;
				sushi = new Nigiri(nigiriType);
				break;
			}
			break;
		case "Roll":
			switch(e.getActionCommand()){
			case "TunaRoll":
				roll.add(new TunaPortion(tunaRollAmt));
				break;
			case "SalmonRoll":
				roll.add(new SalmonPortion(salmonRollAmt));
				break;
			case "EelRoll":
				roll.add(new EelPortion(eelRollAmt));
				break;
			case "CrabRoll":
				roll.add(new CrabPortion(crabRollAmt));
				break;
			case "ShrimpRoll":
				roll.add(new ShrimpPortion(shrimpRollAmt));
				break;
			case "Avocado":
				roll.add(new AvocadoPortion(avocadoRollAmt));
				break;
			case "Rice":
				roll.add(new RicePortion(riceRollAmt));
				break;
			case "Seaweed":
				roll.add(new SeaweedPortion(seaweedRollAmt));
				break;
			}
			break;
		}
		num_ing = roll.size();
		System.out.println(num_ing);
		if(sushi != null || num_ing > 0){
			plates(true);
		}else{
			send.setEnabled(false);
			plates(false);
		}
	}

	public void ings(Boolean state){
		for(int i=0;i<ings.length;i++){
			ings[i].setEnabled(state);
		}
	}

	public void ingsRoll(Boolean state){
		for(int i=0;i<ingsForRoll.length;i++){
			ingsForRoll[i].setEnabled(state);
			System.out.println("lol");
		}
	}

	public void amtRoll(Boolean state){
		for(int i=0; i<amtOfIngs.length; i++){
			amtOfIngs[i].setEnabled(state);
		}
	}

	public void plates(Boolean state){
		for(int i=0; i<plates.length; i++){
			plates[i].setEnabled(state);
		}
	}

	//resets the panel selections
	private int ct;
	public void refresh(){
		sushiType = "";
		plateColor = null;
		sashimiType = null;
		nigiriType = null;
		sushi = null;
		num_ing=0;
		ings(false);
		amtRoll(false);
		sushiTypeGroup.clearSelection();
		ingGroup.clearSelection();
		plateGroup.clearSelection();
		for(int i=0; i<ingsForRoll.length;i++){
			ingsForRoll[i].setSelected(false);
		}
		for(int i=0; i<amtOfIngs.length; i++){
			amtOfIngs[i].setValue(0);
		}
		position_slider.setValue(0);
		at_position = 0;
		goldPriceSlider.setValue(0);
		goldPrice = 0;
		goldPriceSlider.setEnabled(false);
		ingsRoll(false);
		ct = 1;
	}
	//WRITE HELPER METHOD
	@Override
	public void stateChanged(ChangeEvent arg0) {
		goldPrice = goldPriceSlider.getValue()/100.0;
		at_position = position_slider.getValue();

		tunaRollAmt = tunaRollSlider.getValue()/100.0;
		if(tunaRollAmt != 0){
			for(int i=0; i<ingsForRoll.length;i++){
				if(ingsForRoll[i].getText().equals("Tuna") && ct == 0){
					ingsForRoll[i].setEnabled(true);
				}
			}
		}
		salmonRollAmt = salmonRollSlider.getValue()/100.0;
		if(salmonRollAmt != 0){
			for(int i=0; i<ingsForRoll.length;i++){
				if(ingsForRoll[i].getText().equals("Salmon") && ct == 0){
					ingsForRoll[i].setEnabled(true);
				}
			}
		}
		eelRollAmt = eelRollSlider.getValue()/100.0;
		if(eelRollAmt != 0){
			for(int i=0; i<ingsForRoll.length;i++){
				if(ingsForRoll[i].getText().equals("Eel") && ct == 0){
					ingsForRoll[i].setEnabled(true);
				}
			}
		}
		crabRollAmt = crabRollSlider.getValue()/100.0;
		if(crabRollAmt != 0){
			for(int i=0; i<ingsForRoll.length;i++){
				if(ingsForRoll[i].getText().equals("Crab") && ct == 0){
					ingsForRoll[i].setEnabled(true);
				}
			}
		}
		shrimpRollAmt = shrimpRollSlider.getValue()/100.0;
		if(shrimpRollAmt != 0){
			for(int i=0; i<ingsForRoll.length;i++){
				if(ingsForRoll[i].getText().equals("Shrimp") && ct == 0){
					ingsForRoll[i].setEnabled(true);
				}
			}
		}
		avocadoRollAmt = avocadoRollSlider.getValue()/100.0;
		if(avocadoRollAmt != 0){
			for(int i=0; i<ingsForRoll.length;i++){
				if(ingsForRoll[i].getText().equals("Avocado") && ct == 0){
					ingsForRoll[i].setEnabled(true);
				}
			}
		}
		riceRollAmt = riceRollSlider.getValue()/100.0;
		if(riceRollAmt != 0){
			for(int i=0; i<ingsForRoll.length;i++){
				if(ingsForRoll[i].getText().equals("Rice") && ct == 0){
					ingsForRoll[i].setEnabled(true);
				}
			}
		}
		seaweedRollAmt = seaweedRollSlider.getValue()/100.0;
		if(seaweedRollAmt != 0){
			for(int i=0; i<ingsForRoll.length;i++){
				if(ingsForRoll[i].getText().equals("Seaweed") && ct == 0){
					ingsForRoll[i].setEnabled(true);
				}
			}
		}
	}
}
