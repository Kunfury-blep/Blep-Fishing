package Objects;

import java.util.List;

import org.bukkit.inventory.ItemStack;

///
//
///
public class TourneyAdminObject {

	public int Duration;
	public String FishName;
	public List<ItemStack> Rewards;
	public int Cash;

	/**
	 * TourneyAdminObject is used for the tracking of a new tournament throughout the Creation GUI
	 * After completion, the values in this object are transferred over to create a new TourneyAdminObject
	 * @param _duration The duration of the tournament
	 * @param _fishName The fish name of the tournament
	 * @param _rewards The rewards of the tournaments
	 * @param _cash The Cashreward of the tournament
	 */
	public TourneyAdminObject(int _duration, String _fishName, List<ItemStack> _rewards, int _cash) {
		Duration = _duration;
		FishName = _fishName;
		Rewards = _rewards;
		Cash = _cash;
	}
	
}
