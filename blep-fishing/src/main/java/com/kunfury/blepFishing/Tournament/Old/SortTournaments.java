package com.kunfury.blepFishing.Tournament.Old;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.kunfury.blepFishing.Config.Variables;
import com.kunfury.blepFishing.Objects.TournamentObjectOld;

public class SortTournaments {

	/**
	 * Sorts the tournament list
	 * @return Sorted tournament list
	 */
	public List<TournamentObjectOld> Sort(){
		
		List<TournamentObjectOld> tourneys = Variables.Tournaments;
		List<TournamentObjectOld> activeList = new ArrayList<>();
		List<TournamentObjectOld> endedList = new ArrayList<>();
		

		tourneys.forEach(t -> {
			if(!t.HasFinished)
				activeList.add(t);
			else
				endedList.add(t);
		});
		
		Collections.sort(activeList, new Comparator<TournamentObjectOld>() {
			  @Override
			  public int compare(TournamentObjectOld t1, TournamentObjectOld t2) {
			    return t2.StartDate.compareTo(t1.StartDate);
			  }
			});
		
		Collections.sort(endedList, new Comparator<TournamentObjectOld>() {
			  @Override
			  public int compare(TournamentObjectOld t1, TournamentObjectOld t2) {
			    return t2.StartDate.compareTo(t1.StartDate);
			  }
			});
		
		tourneys = activeList;
		
		tourneys.addAll(endedList);
		return tourneys;
	}
	
}
