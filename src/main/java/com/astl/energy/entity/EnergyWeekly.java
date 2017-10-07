package com.astl.energy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class EnergyWeekly implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String start;
	BigDecimal energy;
	
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start =  start;
	}
	
	public BigDecimal getEnergy() {
		return energy;
	}
	public void setEnergy(BigDecimal energy) {
		this.energy = energy;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return start + " " + energy;
	}
	
}
