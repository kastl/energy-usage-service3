package com.astl.energy.control;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.astl.energy.entity.Energy;
import com.astl.energy.entity.EnergyDaily;
import com.astl.energy.entity.EnergyWeekly;

@Stateless
@LocalBean
public class Daily {

	@PersistenceContext(unitName = "energy")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Energy> getDaily() {

		List<Energy> daily = null;
		
		List<Energy> result = null;
		try {

			String start = "2017-08-24 00:00:00";
			String end = "2017-08-25 00:00:00";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date startDate;

			startDate = df.parse(start);

			Date endDate = df.parse(end);

			Query selectDaily = em.createNamedQuery("Energy.findByStartEnd", Energy.class);
			selectDaily.setMaxResults(0);

			selectDaily.setParameter("start", startDate, TemporalType.DATE);
			selectDaily.setParameter("end", endDate, TemporalType.DATE);

			result = selectDaily.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<EnergyDaily> getDailyRolledUpHourly() {
		
		List result = null;
		ArrayList<EnergyDaily> daily = new ArrayList<EnergyDaily>();
		
		try {

			String q = "SELECT FORMATDATETIME(start,'yyyy-MM-dd') as dd, Hour(start) as hour, sum(energy) as hourly FROM energy"+
			" where start >= '2017-08-24'"+
			" and start < '2017-08-25'"+
			" group by FORMATDATETIME(start,'yyyy-MM-dd'), hour"+
			" order by dd";
			
			Query selectDaily = em.createNativeQuery(q);
			selectDaily.setMaxResults(0);

			result = selectDaily.getResultList();
			ListIterator iterator = result.listIterator();
			while (iterator.hasNext()) {
				Object[] o = (Object[])iterator.next();
				EnergyDaily d = new EnergyDaily();
				String o1 = (String)o[0];
				Integer o2 = (Integer)o[1];
				BigDecimal o3 = (BigDecimal)o[2];
				EnergyDaily ed = new EnergyDaily();
				ed.setStart(o1);
				ed.setHour(o2);
				ed.setEnergy(o3);
				daily.add(ed);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daily;
	}
	
public List<EnergyWeekly> getWeeklyRolledUpDaily() {
		
		List result = null;
		ArrayList<EnergyWeekly> daily = new ArrayList<EnergyWeekly>();
		
		try {

			String q = "SELECT FORMATDATETIME(start,'yyyy-MM-dd') as dd, sum(energy) as daily FROM energy"+
			" where start >= '2017-08-24'"+
			" and start < '2017-08-31'"+
			" group by FORMATDATETIME(start,'yyyy-MM-dd')"+
			" order by dd";
			
			Query selectDaily = em.createNativeQuery(q);
			selectDaily.setMaxResults(0);

			result = selectDaily.getResultList();
			ListIterator iterator = result.listIterator();
			while (iterator.hasNext()) {
				Object[] o = (Object[])iterator.next();
				
				String o1 = (String)o[0];
				BigDecimal o2 = (BigDecimal)o[1];
				EnergyWeekly ed = new EnergyWeekly();
				ed.setStart(o1);
				ed.setEnergy(o2);
				daily.add(ed);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daily;
	}
	
}
