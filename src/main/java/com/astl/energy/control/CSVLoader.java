package com.astl.energy.control;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import com.astl.energy.boundary.EnergyFacadeREST;
import com.astl.energy.entity.Energy;

@Singleton
@Startup
@LocalBean
public class CSVLoader {

	@Inject
	EnergyFacadeREST energyFacadeRest;

	@PersistenceContext(unitName = "energy")
	private EntityManager em;

	public static void main(String[] args) {
		new CSVLoader().load();
	}
	
	@PostConstruct
	public void load() {
		try {
			
			Query deleteAll = em.createNamedQuery("Energy.deleteAll");
			int result = deleteAll.executeUpdate();
			
			System.out.println("delete all rows, result = " + result);
			
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("energyusage.csv");
			InputStreamReader inReader = new InputStreamReader(in);

			CSVParser parser = CSVParser.parse(inReader, CSVFormat.RFC4180);

			parser.forEach(record -> {
				if (record.size() == 7 && record.get(0).equals("Electric usage")) {
					Double energyUsed = Double.parseDouble(record.get(4));
					String date = record.get(1);
					String time = record.get(2);
					LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time);

					Energy energy = new Energy();
					energy.setEnergy(BigDecimal.valueOf(energyUsed));
					energy.setStart(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));

					System.out.println(energy);

					energyFacadeRest.create(energy);
				}

			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
