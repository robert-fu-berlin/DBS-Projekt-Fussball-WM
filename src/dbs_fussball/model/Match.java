package dbs_fussball.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import active_record.ActiveRecord;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

public class Match extends ActiveRecord {

	private Cup			cup;

	private String		annotation;
	private Integer		attendace;

	private Set<Event>	events;

	private Stadium		stadium;
	private Team		teamA, teamB;

	private Set<Person>	startingLineUpTeamA, startingLineUpTeamB;

	private Long		startingTime;
	/**
	 * Default constructor used for reflection, should not be called.
	 */
	public Match() {

	}

	public Match(Cup cup) {
		events = new HashSet<Event>();
		this.cup = cup;
	}

	public Match(Cup cup, Team teamA, Team teamB) {
		this(cup);
		this.teamA = teamA;
		this.teamB = teamB;
	}

	public Cup getCup() {
		return cup;
	}

	public Stadium getStadium() {
		return stadium;
	}

	public Date getStatingTime() {
		return new Date(startingTime);
	}

	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime.getTime();
	}

	public boolean addPlayerToLineUpTeamA(Person player) {
		return startingLineUpTeamA.add(player);
	}

	public boolean addPlayerToLineUpTeamA(Person player, Person... morePlayers) {
		return startingLineUpTeamA.add(player) | Collections.addAll(startingLineUpTeamA, morePlayers);
	}

	public boolean lineUpTeamAContains(Person player) {
		return startingLineUpTeamA.contains(player);
	}

	public boolean removePlayerFromLineUpTeamA(Person player) {
		return startingLineUpTeamA.remove(player);
	}

	public Iterable<Person> playersOfLineUpTeamA() {
		return Iterables.unmodifiableIterable(startingLineUpTeamA);
	}

	public Iterator<Person> playersOfLineUpTeamAIterator() {
		return Iterators.unmodifiableIterator(startingLineUpTeamA.iterator());
	}
	public boolean addPlayerToLineUpTeamB(Person player) {
		return startingLineUpTeamB.add(player);
	}

	public boolean addPlayerToLineUpTeamB(Person player, Person... morePlayers) {
		return startingLineUpTeamB.add(player) | Collections.addAll(startingLineUpTeamB, morePlayers);
	}

	public boolean lineUpTeamBContains(Person player) {
		return startingLineUpTeamB.contains(player);
	}

	public boolean removePlayerFromLineUpTeamB(Person player) {
		return startingLineUpTeamB.remove(player);
	}

	public Iterable<Person> playersOfLineUpTeamB() {
		return Iterables.unmodifiableIterable(startingLineUpTeamB);
	}

	public Iterator<Person> playersOfLineUpTeamBIterator() {
		return Iterators.unmodifiableIterator(startingLineUpTeamB.iterator());
	}
	public boolean addEvent(Event event) {
		return events.add(event);
	}

	public boolean addEvent(Event event, Event... moreEvents) {
		return events.add(event) | Collections.addAll(events, moreEvents);
	}

	public boolean containsEvent(Event event) {
		return events.contains(event);
	}

	public boolean removeEvent(Event event) {
		return events.remove(event);
	}

	public Iterable<Event> events() {
		return Iterables.unmodifiableIterable(events);
	}

	public Iterator<Event> eventsIterator() {
		return Iterators.unmodifiableIterator(events.iterator());
	}

	public void setTeamA(Team teamA) {
		this.teamA = teamA;
	}

	public Team getTeamA() {
		return teamA;
	}

	public void setTeamB(Team teamB) {
		this.teamB = teamB;
	}

	public Team getTeamB() {
		return teamB;
	}

	public void setAttendace(Integer attendace) {
		this.attendace = attendace;
	}

	public Integer getAttendace() {
		return attendace;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getAnnotation() {
		return annotation;
	}
}
