package com.trecapps.falsehoodsearch.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table
@Entity
public class PublicFigure implements Comparable<PublicFigure>
{
	@Override
	public int compareTo(PublicFigure o) {
		return id.compareTo(o.getId());
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	@Column
	String firstname;
	
	@Column
	String middleNames;
	
	@Column
	String lastName;


	/**
	 * @param id
	 * @param firstname
	 * @param middleNames
	 * @param lastName
	 */
	public PublicFigure(Long id, String firstname, String middleNames, String lastName) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.middleNames = middleNames;
		this.lastName = lastName;
	}
	
	


	@Override
	public String toString() {
		return "PublicFigure [id=" + id + ", firstname=" + firstname + ", middleNames=" + middleNames + ", lastName="
				+ lastName + "]";
	}


	/**
	 * 
	 */
	public PublicFigure() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the middleNames
	 */
	public String getMiddleNames() {
		return middleNames;
	}

	/**
	 * @param middleNames the middleNames to set
	 */
	public void setMiddleNames(String middleNames) {
		this.middleNames = middleNames;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PublicFigure))
			return false;
		PublicFigure other = (PublicFigure) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
