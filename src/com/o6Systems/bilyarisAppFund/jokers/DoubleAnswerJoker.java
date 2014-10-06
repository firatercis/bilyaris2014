package com.o6Systems.bilyarisAppFund.jokers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.o6Systems.bilyarisAppFund.BilYarisAppState;

public class DoubleAnswerJoker extends BilYarisJoker{
	final static int MAX_USER_TIME = 60;
	@Override
	public void apply(BilYarisAppState state) {
		state.setUsingDoubleAnswer(true);
		quantity--;
	}
	
}
