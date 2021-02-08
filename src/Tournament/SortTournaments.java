package Tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Miscellaneous.Variables;
import Objects.TournamentObject;

public class SortTournaments {

	
	public List<TournamentObject> Sort(){
		
		List<TournamentObject> tourneys = Variables.Tournaments;
		List<TournamentObject> activeList = new ArrayList<>();
		List<TournamentObject> endedList = new ArrayList<>();
		

		tourneys.forEach(t -> {
			if(!t.HasFinished)
				activeList.add(t);
			else
				endedList.add(t);
		});
		
		Collections.sort(activeList, new Comparator<TournamentObject>() {
			  @Override
			  public int compare(TournamentObject t1, TournamentObject t2) {
			    return t2.StartDate.compareTo(t1.StartDate);
			  }
			});
		
		Collections.sort(endedList, new Comparator<TournamentObject>() {
			  @Override
			  public int compare(TournamentObject t1, TournamentObject t2) {
			    return t2.StartDate.compareTo(t1.StartDate);
			  }
			});
		
		tourneys = activeList;
		
		tourneys.addAll(endedList);
		return tourneys;
	}
	
}
