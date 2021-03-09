package Objects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import Miscellaneous.Formatting;
import Miscellaneous.Variables;
import Tournament.Tournament;

public class TournamentObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6710261954442171522L;
	public LocalDateTime StartDate; //When the tournament began
	//public int Duration; //How long the tournament lasts
	public Number newDuration;
	public int CashReward;
	public String FishName;
	public BaseFishObject Fish;
	public LocalDateTime EndDate;
	public boolean HasFinished = true;
	
	public String Winner = Variables.Messages.getString("noWinner");
	//public List<ItemStack> Rewards = new ArrayList<>(); //The item rewards of the tournament
	
	private List<String> rewardNames = new ArrayList<>();
	private List<Integer> rewardCounts = new ArrayList<>();

	/**
	 * The tournamentobject
     * @param _duration the duration of the tournament
     * @param _fishName the fish the tournament got created on
     * @param _rewards the rewards of the tournament
     * @param _cash the cash rewards of the tournament
     */
	public TournamentObject(Number _duration, String _fishName, List<ItemStack> _rewards, int _cash) {
		newDuration = _duration;
		StartDate = LocalDateTime.now();
		FishName = _fishName;
		CashReward = _cash;
		
		if(_rewards != null) {
			for(ItemStack i : _rewards) {
				rewardNames.add(i.getType().name());
				rewardCounts.add(i.getAmount());
			}
		}
		
		
		if(!_fishName.equalsIgnoreCase("ALL")) {
			for(BaseFishObject fish : Variables.BaseFishList) {
				if(_fishName.equalsIgnoreCase(fish.Name)) {
					Fish = fish;
					break;
				}
			}
		}
		EndDate = StartDate.plusSeconds((long)(_duration.doubleValue() * 60 * 60));

		new Tournament().StartTimer(StartDate.until(EndDate, ChronoUnit.MILLIS), this);
		
	}
	
	public List<ItemStack> GetRewards(){
		
		List<ItemStack> items = new ArrayList<>();
		for(int i = 0; i < rewardNames.size(); i++) {
			ItemStack itemStack = new ItemStack(Material.getMaterial(rewardNames.get(i)), rewardCounts.get(i));
			items.add(itemStack);
		}
		
		return items;
	}

	/**
	 * Get the remaining time of the tournament
	 * @return the remaining time
	 */
	public String GetRemainingTime() {
		LocalDateTime now = LocalDateTime.now();

	    long diff = ChronoUnit.MILLIS.between(now, EndDate);
		
		return Formatting.TimeFormat(diff);		
	}

	/**
	 * Gets the end date but in formatted version
	 * @return formatted version of the enddate
	 */
	public String GetFormattedEndDate() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		String formatDateTime = EndDate.format(formatter);
		
		return formatDateTime;
	}
	
	
}
