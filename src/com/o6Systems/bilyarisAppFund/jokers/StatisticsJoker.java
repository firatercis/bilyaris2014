package com.o6Systems.bilyarisAppFund.jokers;

import java.util.Random;

import com.o6Systems.bilyarisAppFund.BilYarisAppState;
import com.o6Systems.bilyarisAppFund.Question;

public class StatisticsJoker extends BilYarisJoker{

	final static int RANDOM_BASE = 10;
	
	int wrongRatePerChoice = 1; // 1/10 ihtimal ile yanlis bir secenege gidiliyor

	final static int N_TRIALS = 100;
	
	
	@Override
	public void apply(BilYarisAppState state) {
		Random randomGenerator = new Random();
		int correctAnswer = state.getCurrentQuestion().getAnswer();
	
		int[] statistics = new int[Question.N_POSSIBLE_ANSWERS];
		
		statistics[correctAnswer] = N_TRIALS;
		for(int i=0; i< statistics.length; i++){
			if(i != correctAnswer){
				for(int currentTrial = 0; currentTrial < N_TRIALS; currentTrial++){
					int currentRandom = randomGenerator.nextInt(RANDOM_BASE);
					if(currentRandom < wrongRatePerChoice){
						statistics[i] += 1;
						statistics[correctAnswer] -= 1;
					}
				}
			}
		}
		state.setStatictics(statistics);
		quantity--;
	}
	
}
