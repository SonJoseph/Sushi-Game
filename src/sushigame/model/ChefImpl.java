package sushigame.model;

import java.util.ArrayList;
import java.util.List;

import comp401.sushi.Plate;
import comp401.sushi.RedPlate;
import comp401.sushi.Sushi;
import comp401.sushi.IngredientPortion;
import comp401.sushi.Nigiri.NigiriType;
import comp401.sushi.Plate.Color;
import comp401.sushi.Sashimi.SashimiType;

/* Currently the scoreboard displays the chefs in the order of their account balance from highest to lowest. 
 * Modify the game so that each chef also keeps track of the total amount of food consumed by customers and 
 * the total amount of food spoiled on the belt. (HINT: this will require you to change the interface Chef 
 * and implementation ChefImpl in the sushigame.model package.). Then rewrite ScoreboardWidget so that one 
 * can select between showing the chefs in balance order (highest to lowest), food sold order (highest to lowest) 
 * or food spoiled order (lowest to highest).
 */
public class ChefImpl implements Chef, BeltObserver {

	private double balance;
	private List<HistoricalPlate> plate_history;
	private String name;
	private ChefsBelt belt;
	private boolean already_placed_this_rotation;
	private int plate_sold_ct;
	private int plate_spoiled_ct;
	private int amt;
	
	public ChefImpl(String name, double starting_balance, ChefsBelt belt) {
		this.name = name;
		this.balance = starting_balance;
		this.belt = belt;
		belt.registerBeltObserver(this);
		already_placed_this_rotation = false;
		plate_history = new ArrayList<HistoricalPlate>();
		plate_sold_ct = 0;
		plate_spoiled_ct = 0;
		amt = 0;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String n) {
		this.name = n;
	}
	
	@Override
	public int getSold(){
		return plate_sold_ct;
	}
	
	@Override
	public int getSpoiled(){
		return plate_spoiled_ct;
	}

	@Override
	public HistoricalPlate[] getPlateHistory(int history_length) {
		if (history_length < 1 || (plate_history.size() == 0)) {
			return new HistoricalPlate[0];
		}

		if (history_length > plate_history.size()) {
			history_length = plate_history.size();
		}
		return plate_history.subList(plate_history.size()-history_length, plate_history.size()-1).toArray(new HistoricalPlate[history_length]);
	}

	@Override
	public HistoricalPlate[] getPlateHistory() {
		return getPlateHistory(plate_history.size());
	}

	@Override
	public double getBalance() {
		return balance;
	}

	@Override
	public void makeAndPlacePlate(Plate plate, int position) 
			throws InsufficientBalanceException, BeltFullException, AlreadyPlacedThisRotationException {

		if (already_placed_this_rotation) {
			throw new AlreadyPlacedThisRotationException();
		}
		
		if (plate.getContents().getCost() > balance) {
			throw new InsufficientBalanceException();
		}
		belt.setPlateNearestToPosition(plate, position);
		balance = balance - plate.getContents().getCost();
		already_placed_this_rotation = true;
	}

	@Override
	public void handleBeltEvent(BeltEvent e) {
		if (e.getType() == BeltEvent.EventType.PLATE_CONSUMED) {
			Plate plate = ((PlateEvent) e).getPlate();
			if (plate.getChef() == this) {
				amt += plate.getContents().getCalories();
				plate_sold_ct++;
				balance += plate.getPrice();
				Customer consumer = belt.getCustomerAtPosition(((PlateEvent) e).getPosition());
				plate_history.add(new HistoricalPlateImpl(plate, consumer));
			}
		} else if (e.getType() == BeltEvent.EventType.PLATE_SPOILED) {
			Plate plate = ((PlateEvent) e).getPlate();
			if (plate.getChef() == this){
				plate_spoiled_ct++;
				plate_history.add(new HistoricalPlateImpl(plate, null));
			}
		} else if (e.getType() == BeltEvent.EventType.ROTATE) {
			already_placed_this_rotation = false;
		}
	}
	
	
	
	@Override
	public boolean alreadyPlacedThisRotation() {
		return already_placed_this_rotation;
	}
	
	@Override
	public int getAmountConsumed() {
		return amt;
	}

	
}
