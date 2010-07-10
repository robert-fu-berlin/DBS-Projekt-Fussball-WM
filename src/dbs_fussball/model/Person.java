package dbs_fussball.model;

import java.util.Date;

import active_record.ActiveRecord;

public class Person extends ActiveRecord {
	private String firstName, lastName, stageName, club;
	private Date birthDate;
	private Float height, weight;
	
	public Person() {
		
	}
	
	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Person(String stageName) {
		this.stageName = stageName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getStageName() {
		return stageName;
	}
	
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	
	public String getClub() {
		return club;
	}
	
	public void setClub(String club) {
		this.club = club;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public Float getHeight() {
		return height;
	}
	
	public void setHeight(Float height) {
		this.height = height;
	}
	
	public Float getWeight() {
		return weight;
	}
	
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	
}
