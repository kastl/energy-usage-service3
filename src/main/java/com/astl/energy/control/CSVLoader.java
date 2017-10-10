package com.astl.energy.control;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.astl.energy.boundary.EnergyFacadeREST;
import com.astl.energy.entity.Energy;

@Singleton
@Startup
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

			try (Scanner scanner = new Scanner(in)) {
				// skip first line
				if (scanner.hasNextLine()) {
					scanner.nextLine();
				}

				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					String[] record = line.split(",");
					Double energyUsed = Double.parseDouble(record[4]);
					String date = record[1];
					String time = record[2];
					LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time);

					Energy energy = new Energy();
					energy.setEnergy(BigDecimal.valueOf(energyUsed));
					energy.setStart(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));

					System.out.println(energy);

					energyFacadeRest.create(energy);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
