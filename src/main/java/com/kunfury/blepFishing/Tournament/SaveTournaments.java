package com.kunfury.blepFishing.Tournament;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.kunfury.blepFishing.Setup;

import com.kunfury.blepFishing.Miscellaneous.Variables;

public class SaveTournaments {
	/**
	 * Saves all the tournaments
	 */
	public SaveTournaments() {
		try {
			String tourneyPath = Setup.dataFolder + "/Data" + "/tournaments.data";
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(tourneyPath));
		    output.writeObject(Variables.Tournaments);
		    output.close();
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
	}
	
	
	
}
