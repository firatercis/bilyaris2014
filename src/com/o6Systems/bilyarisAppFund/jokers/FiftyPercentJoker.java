package com.o6Systems.bilyarisAppFund.jokers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.o6Systems.bilyarisAppFund.BilYarisAppState;

public class FiftyPercentJoker extends BilYarisJoker{

	@Override
	public void apply(BilYarisAppState state) {
		List<Integer> lstChoiceIDs = new ArrayList<Integer>();
		int nChoices = state.getChoicesAvailable().length;
		
		for(int i=0; i<nChoices; i++){
			if(i!=state.getCurrentQuestion().getAnswer())
				lstChoiceIDs.add(i);
		}
		Collections.shuffle(lstChoiceIDs);
		for(int i=0; i< nChoices/2; i++){
			int currentChoiceToEliminate = lstChoiceIDs.get(i);
			state.eliminateChoice(currentChoiceToEliminate);
		}
		quantity--;
	}
	
}
