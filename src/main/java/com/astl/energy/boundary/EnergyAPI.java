package com.astl.energy.boundary;


import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.astl.energy.control.CSVLoader;
import com.astl.energy.control.Daily;
import com.astl.energy.entity.EnergyDaily;
import com.astl.energy.entity.EnergyWeekly;

@Stateless
@Path("api")
public class EnergyAPI {

	@Inject
	CSVLoader loader;
	

	@Inject
	Daily daily;
	
    @GET
    @Path("load")
    @Produces(MediaType.APPLICATION_JSON)
    public String result() {
    	loader.load();
    	
        return "{\"status\":\"success\"}";
    }
    
    @GET
    @Path("daily")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EnergyDaily> daily() {
    	List<EnergyDaily> result = daily.getDailyRolledUpHourly();
    	
    	return result;
    }
    
    @GET
    @Path("weekly")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EnergyWeekly> weekly() {
    	List<EnergyWeekly> result = daily.getWeeklyRolledUpDaily();
    	
    	return result;
    }
}