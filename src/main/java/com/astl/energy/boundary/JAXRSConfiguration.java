package com.astl.energy.boundary;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("resources")
public class JAXRSConfiguration extends Application {

	  @Override
	    public Set<Class<?>> getClasses() {
	        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
	        addRestResourceClasses(resources);
	        
	        return resources;
	    }

	    /**
	     * Do not modify addRestResourceClasses() method.
	     * It is automatically populated with
	     * all resources defined in the project.
	     * If required, comment out calling this method in getClasses().
	     */
	    private void addRestResourceClasses(Set<Class<?>> resources) {
	        resources.add(EnergyFacadeREST.class);
	        resources.add(EnergyAPI.class);
	    }
	    
}
