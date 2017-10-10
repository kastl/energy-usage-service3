package com.astl.energy.control;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;

import com.astl.energy.boundary.EnergyEvent;

@Singleton
@Startup
public class EventObserver {
	public void onEvent(@Observes @Default EnergyEvent event) {
		System.out.println("EventObserver Observed energy event - " + event.getEnergy() + " energy");
	}
}
