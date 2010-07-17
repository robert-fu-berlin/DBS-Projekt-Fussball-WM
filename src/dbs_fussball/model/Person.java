package dbs_fussball.model;

import java.util.Date;

import com.google.common.base.Equivalences;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.primitives.Primitives;

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
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Person))
			return false;
		
		Person other = (Person) obj;
		
		if (!Objects.equal(this.id, other.id))
			return false;
		
		if (!Objects.equal(this.firstName, other.firstName))
			return false;
	
		if (!Objects.equal(this.lastName, other.lastName))
			return false;
		
		if (!Objects.equal(this.stageName, other.stageName))
			return false;
		
		if (!Objects.equal(this.club, other.club))
			return false;
		
		if (!Objects.equal(this.birthDate, other.birthDate))
			return false;
		
		if (!Objects.equal(this.height, other.height))
			return false;
		
		if (!Objects.equal(this.weight, other.weight))
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		if (id != null)
			return id.hashCode();
		else
			return Objects.hashCode(firstName, lastName, stageName);
	}
	
	/**
	 * Convenience method to access the player's names.
	 * 
	 * TODO make that more efficient
	 * @return
	 */
	public String getDisplayName() {
		String[] names = new String[3];
		
		if (firstName != null)
			names[0] = firstName;
		
		if (stageName != null)
			names[1] = "È"+ stageName + "Ç";
		
		if (lastName != null)
			names[2] = lastName;
		
		return Joiner.on(" ").skipNulls().join(names);
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
