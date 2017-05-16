package sushigame.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import sushigame.model.Belt;
import sushigame.model.BeltEvent;
import sushigame.model.BeltObserver;
import sushigame.model.Chef;
import sushigame.model.HighToLowSoldSushiConsumedComparator;
import sushigame.model.LowToHighSpoiledPlateComparator;
import sushigame.model.SushiGameModel;

public class ScoreboardWidget extends JPanel implements BeltObserver {

	private SushiGameModel game_model;
	private JTabbedPane tabPane;
	private JLabel balanceScoreBoardDisplay;
	private JLabel soldScoreBoardDisplay;
	private JLabel spoiledScoreBoardDisplay;
	
	
	public ScoreboardWidget(SushiGameModel gm) {
		game_model = gm;
		game_model.getBelt().registerBeltObserver(this);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.RED);
		
		tabPane = new JTabbedPane();
		tabPane.setForeground(Color.WHITE);
		tabPane.setFont(new Font("Arial", Font.PLAIN, 15));
		
		JPanel balanceScoreBoard = new JPanel(){
			public Dimension getPreferredSize(){
				Dimension size = super.getPreferredSize();
				size.width += 150;
				return size;
			}
		};
		balanceScoreBoardDisplay = new JLabel(makeBalanceScoreboard());
		balanceScoreBoard.setBackground(Color.RED);
		balanceScoreBoardDisplay.setForeground(Color.WHITE);
		balanceScoreBoardDisplay.setFont(new Font("Arial", Font.BOLD, 20));
		balanceScoreBoard.add(balanceScoreBoardDisplay);
		
		JPanel soldScoreBoard = new JPanel();
		soldScoreBoardDisplay = new JLabel(makeSushiConsumedSoldScoreboard());
		soldScoreBoardDisplay.setForeground(Color.WHITE);
		soldScoreBoard.setBackground(Color.RED);
		soldScoreBoardDisplay.setFont(new Font("Arial", Font.BOLD, 20));
		soldScoreBoard.add(soldScoreBoardDisplay);
		
		JPanel spoiledScoreBoard = new JPanel();
		spoiledScoreBoardDisplay = new JLabel(makePlateSpoiledScoreboard());
		spoiledScoreBoardDisplay.setForeground(Color.WHITE);
		spoiledScoreBoard.setBackground(Color.RED);
		spoiledScoreBoardDisplay.setFont(new Font("Arial", Font.BOLD, 20));
		spoiledScoreBoard.add(spoiledScoreBoardDisplay);
		
		tabPane.addTab("Balance", balanceScoreBoard);
		tabPane.addTab("Sushi Sold", soldScoreBoard);
		tabPane.addTab("Plates Spoiled", spoiledScoreBoard);
		tabPane.setBackground(Color.RED);
		
		JLabel title = new JLabel("Scoreboard");
		title.setFont(new Font("Arial",Font.BOLD, 40));
		title.setForeground(Color.WHITE);
		
		add(title);
		add(tabPane);
	}


	private String makeBalanceScoreboard() {
		String sb_html = "<html>";
		// Create an array of all chefs and sort by balance.
		Chef[] opponent_chefs= game_model.getOpponentChefs();
		Chef[] chefs = new Chef[opponent_chefs.length+1];
		chefs[0] = game_model.getPlayerChef();
		for (int i=1; i<chefs.length; i++) {
			chefs[i] = opponent_chefs[i-1];
		}
		Arrays.sort(chefs, new HighToLowBalanceComparator());
		
		for (Chef c : chefs) {
			sb_html += c.getName() + ": $" + Math.round(c.getBalance()*100.0)/100.0 + " <br>";
		}
		return sb_html;
	}
	
	private String makeSushiConsumedSoldScoreboard() {
		String sb_html = "<html>";
		// Create an array of all chefs and sort by balance.
		Chef[] opponent_chefs= game_model.getOpponentChefs();
		Chef[] chefs = new Chef[opponent_chefs.length+1];
		chefs[0] = game_model.getPlayerChef();
		for (int i=1; i<chefs.length; i++) {
			chefs[i] = opponent_chefs[i-1];
		}
		Arrays.sort(chefs, new HighToLowSoldSushiConsumedComparator());
		
		for (Chef c : chefs) {
			sb_html += c.getName()+": "+c.getAmountConsumed()+" calories"+"<br>";
		}
		return sb_html;
	}
	
	private String makePlateSpoiledScoreboard() {
		String sb_html = "<html>";
		// Create an array of all chefs and sort by balance.
		Chef[] opponent_chefs= game_model.getOpponentChefs();
		Chef[] chefs = new Chef[opponent_chefs.length+1];
		chefs[0] = game_model.getPlayerChef();
		for (int i=1; i<chefs.length; i++) {
			chefs[i] = opponent_chefs[i-1];
		}
		Arrays.sort(chefs, new LowToHighSpoiledPlateComparator());
		
		for (Chef c : chefs) {
			sb_html += c.getName()+": "+c.getSpoiled()+"plates <br>";
		}
		return sb_html;
	}
	

	public void refresh() {
		balanceScoreBoardDisplay.setText(makeBalanceScoreboard());
		soldScoreBoardDisplay.setText(makeSushiConsumedSoldScoreboard());
		spoiledScoreBoardDisplay.setText(makePlateSpoiledScoreboard());
		add(tabPane);
	}
	
	@Override
	public void handleBeltEvent(BeltEvent e) {
		if (e.getType() == BeltEvent.EventType.ROTATE) {
			refresh();
		}		
	}

}
