package com.astl.energy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({ 
	@NamedQuery(name = "Energy.findAll", query = "SELECT e FROM Energy e"),
	@NamedQuery(name = "Energy.findByStartEnd", query = "SELECT e FROM Energy e WHERE e.start >= :start and e.start < :end"),
	@NamedQuery(name = "Energy.findByEnergy", query = "SELECT e FROM Energy e WHERE e.energy = :energy") ,
	@NamedQuery(name = "Energy.deleteAll", query = "DELETE FROM Energy e") 
	})
public class Energy implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "start")
	@Temporal(TemporalType.TIMESTAMP)
	private Date start;
	
	@Column(name = "energy")
	private BigDecimal energy;

	public Energy() {
	}

	public Energy(Date start) {
		this.start = start;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public BigDecimal getEnergy() {
		return energy;
	}

	public void setEnergy(BigDecimal energy) {
		this.energy = energy;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (start != null ? start.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Energy)) {
			return false;
		}
		Energy other = (Energy) object;
		if ((this.start == null && other.start != null) || (this.start != null && !this.start.equals(other.start))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "start=" + start + " energy=" + energy;
	}

}
