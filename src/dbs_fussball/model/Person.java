package dbs_fussball.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import active_record.ActiveRecord;
import active_record.Inverse;
import active_record.ValidationFailure;

import com.google.common.base.Joiner;

public class Person extends ActiveRecord {

	private String firstName, lastName, stageName, club;
	private Date birthDate;
	private Float height, weight;

	@Inverse("dbs_fussball.model.Team.players")
	private Set<Team> playedTeams;

	/**
	 * Public constructor for reflection.
	 * TODO find a way to reduce the visibility of this constructor without
	 * messing up the ActiveRecordMapper
	 */
	public Person() {
		this.playedTeams = new HashSet<Team>();
	}

	public Person(String firstName, String lastName) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Person(String stageName) {
		this();
		this.stageName = stageName;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Person))
			return false;

		Person other = (Person) obj;

		if (this.id == null || other.id == null)
			return false;

		return this.id.equals(other.id);
	}

	/**
	 * Convenience method to access the player's names.
	 *
	 * TODO make that more efficient
	 * @return
	 */
	public String getDisplayName() {
		String[] names = new String[3];

		if (firstName != null) {
			names[0] = firstName;
		}

		if (stageName != null) {
			names[1] = stageName;
		}

		if (lastName != null) {
			names[2] = lastName;
		}

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

	/**
	 * Adds a team in which this person is a player
	 * @param playedTeam
	 */
	void createInversePlayed(Team playedTeam) {
		if (playedTeams == null) {
			playedTeams = new HashSet<Team>();
		}
		this.playedTeams.add(playedTeam);
	}

	@Override
	public List<ValidationFailure> validate() {
		List<ValidationFailure> failureList = new ArrayList<ValidationFailure>();
		if ((firstName == null || lastName == null) && stageName == null) {
			failureList.add(new ValidationFailure("A person must have either first and last or stage name"));
		}
		if (weight != null && (weight < 0 || weight == Float.NaN || weight == Float.POSITIVE_INFINITY)) {
			failureList.add(new ValidationFailure("Weight must be positive, but not infinit"));
		}
		if (height != null && (height < 0 || height == Float.NaN || height == Float.POSITIVE_INFINITY)) {
			failureList.add(new ValidationFailure("Height must be positive, but not infinit"));
		}
		return failureList;
	}

	@Override
	public List<ValidationFailure> validateAssociated() {
		List<ValidationFailure> failureList = validate();
		for (Team t : playedTeams) {
			failureList.addAll(t.validate());
		}
		return failureList;
	}

	@Override
	public String toString() {
		return (firstName == null ? "" : firstName + " ") + (stageName == null ? "" : stageName + " ") + (lastName == null ? "" : lastName);
	}

}
