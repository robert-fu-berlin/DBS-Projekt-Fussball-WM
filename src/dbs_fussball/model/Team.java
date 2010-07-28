package dbs_fussball.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import active_record.ActiveRecord;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

/**
 * This class represents teams taking part in world cups.
 * TODO write proper constructors, consider a builder
 *
 * @author Robert B�hnke
 */
public class Team extends ActiveRecord {
	private Set<Person>	associates;
	private FifaCountry	country;
	private Set<Person>	players;
	private Person		trainer, assistantTrainer, doctor;

	private Set<Match> isTeamAMatches;

	private Set<Match> isTeamBMatches;

	/**
	 * Public constructor for reflection. Should <b>not</b> be called.
	 * TODO find a way to reduce the visibility of this constructor without
	 * messing up the ActiveRecordMapper
	 */
	public Team() {

	}

	public Team(FifaCountry country) {
		players = new HashSet<Person>();
		associates = new HashSet<Person>();
		this.country = country;
	}

	public boolean addAssociate(Person associate) {
		associate.createInverseAssociated(this);
		return associates.add(associate);
	}

	public boolean addAssociate(Person associate, Person... moreAssociates) {
		associate.createInverseAssociated(this);
		for (Person person : moreAssociates) {
			person.createInverseAssociated(this);
		}
		return associates.add(associate) | Collections.addAll(associates, moreAssociates);
	}

	public boolean containsAssociate(Person associate) {
		return associates.contains(associate);
	}

	public boolean removeAssociate(Person associate) {
		return associates.remove(associate);
	}

	public Iterable<Person> associates() {
		return Iterables.unmodifiableIterable(associates);
	}

	public Iterator<Person> associatesIterator() {
		return Iterators.unmodifiableIterator(associates.iterator());
	}

	public boolean addPlayer(Person player) {
		player.createInversePlayed(this);
		return players.add(player);
	}

	public boolean addPlayer(Person player, Person... morePlayers) {
		player.createInversePlayed(this);
		for (Person person : morePlayers) {
			person.createInversePlayed(this);
		}
		return players.add(player) | Collections.addAll(players, morePlayers);
	}

	public boolean containsPlayer(Person associate) {
		return players.contains(associate);
	}

	public boolean removePlayer(Person associate) {
		return players.remove(associate);
	}

	public Iterable<Person> players() {
		return Iterables.unmodifiableIterable(players);
	}

	public Iterator<Person> playersIterator() {
		return Iterators.unmodifiableIterator(players.iterator());
	}

	public Person getAssitantTrainer() {
		return assistantTrainer;
	}

	public Person getDoctor() {
		return doctor;
	}

	public FifaCountry getCountry() {
		return country;
	}

	public Person getTrainer() {
		return trainer;
	}

	public void setAssitantTrainer(Person assitantTrainer) {
		this.assistantTrainer = assitantTrainer;
		assitantTrainer.createInverseAssistantTrained(this);
	}

	public void setDoctor(Person doctor) {
		this.doctor = doctor;
		doctor.createInverseDoctor(this);
	}

	public void setNation(FifaCountry nation) {
		this.country = nation;
	}

	public void setTrainer(Person trainer) {
		this.trainer = trainer;
		trainer.createInverseTrained(this);
	}
	/**
	 * Adds a match in which this team is team A
	 * @param match
	 */
	void createInverseLinedUpForTeamA (Match match) {
		if (isTeamAMatches == null)
			isTeamAMatches = new HashSet<Match>();
		this.isTeamAMatches.add(match);
	}
	/**
	 * Adds a match in which this team is team B
	 * @param match
	 */
	void createInverseLinedUpForTeamB (Match match) {
		if (isTeamBMatches == null)
			isTeamBMatches = new HashSet<Match>();
		this.isTeamBMatches.add(match);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Team))
			return false;

		Team other = (Team) obj;

		if (this.id == null || other.id == null)
			return false;

		return this.id.equals(other.id);
	}

}

