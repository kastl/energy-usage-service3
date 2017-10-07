package com.astl.energy.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class EnergyDaily implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String start;
	Integer hour;
	BigDecimal energy;
	
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start =  start;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
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
		return start + " " + hour + " " + energy;
	}
	
}
