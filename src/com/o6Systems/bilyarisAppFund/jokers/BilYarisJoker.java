package com.o6Systems.bilyarisAppFund.jokers;

import com.o6Systems.bilyarisAppFund.BilYarisAppState;

public abstract class BilYarisJoker {
	
	String name;
	int quantity=1;
	public abstract void apply(BilYarisAppState state);
	
	public boolean available(){
		return quantity > 0;
	}
	
}
