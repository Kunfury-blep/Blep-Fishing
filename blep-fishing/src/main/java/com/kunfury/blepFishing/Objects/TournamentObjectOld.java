package com.kunfury.blepFishing.Objects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.kunfury.blepFishing.Miscellaneous.Formatting;
import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Tournament.Old.Tournament;

public class TournamentObjectOld implements Serializable {

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
	public boolean HasFinished;
	
	public String Winner = Variables.getMessage("noWinner");
	//public List<ItemStack> Rewards = new ArrayList<>(); //The item rewards of the tournament
	
	private final List<String> rewardNames = new ArrayList<>();
	private final List<Integer> rewardCounts = new ArrayList<>();

	private List<String> SerializedItems = new ArrayList<>();
	/**
	 * The tournamentobject
     * @param _duration the duration of the tournament
     * @param _fishName the fish the tournament got created on
     * @param _rewards the rewards of the tournament
     * @param _cash the cash rewards of the tournament
     */
	public TournamentObjectOld(Number _duration, String _fishName, List<ItemStack> _rewards, int _cash) {
		newDuration = _duration;
		StartDate = LocalDateTime.now();
		FishName = _fishName;
		CashReward = _cash;

		if(_rewards != null) {
			SerializedItems = Variables.SerializeItemList(_rewards);
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

		//Announce to players that a new tournament has begun
		Bukkit.broadcastMessage(Variables.Prefix + "A new tournament has been started!");
		if(FishName.equalsIgnoreCase("ALL")) Bukkit.broadcastMessage(Variables.Prefix + "Catch any fish to compete!");
		else Bukkit.broadcastMessage(Variables.Prefix + "Catch a " + FishName + " to compete!");

		new Tournament().StartTimer(StartDate.until(EndDate, ChronoUnit.MILLIS), this);
	}
	
	public List<ItemStack> GetRewards(){
		return Variables.DeserializeItemList(SerializedItems);
	}

	/**
	 * Get the remaining time of the tournament
	 * @return the remaining time
	 */
	public String GetRemainingTime() {
		LocalDateTime now = LocalDateTime.now();

	    long diff = ChronoUnit.MILLIS.between(now, EndDate);
		//TODO: Definitely bring this over
		return Formatting.asTime(diff);
	}

	/**
	 * Gets the end date but in formatted version
	 * @return formatted version of the enddate
	 */
	public String GetFormattedEndDate() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		return EndDate.format(formatter);
	}

	public List<FishObject> GetWinners(){
		List<FishObject> tourneyFish = GetTournamentFish();

		if(tourneyFish.size() > 0) { //If any fish have been caught
			tourneyFish.sort(Collections.reverseOrder());

			if (tourneyFish.size() > 3)
				tourneyFish.subList(3, tourneyFish.size()).clear();
		}
		return tourneyFish;
	}

	public List<FishObject> GetTournamentFish(){
		List<FishObject> tourneyFish = new ArrayList<>();
		Objects.requireNonNull(Variables.getFishList("ALL")).forEach(f -> { //Gets all fish from the list to check against what was caught
			//Checks if the fish is the correct type and was caught after the start date
			if((FishName.equalsIgnoreCase("ALL") || f.Name.equalsIgnoreCase(FishName))
					&& f.DateCaught.isAfter(StartDate) && f.DateCaught.isBefore(EndDate)) {
				tourneyFish.add(f);
			}
		});
		return tourneyFish;
	}
}
