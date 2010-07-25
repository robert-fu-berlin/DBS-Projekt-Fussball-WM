package dbs_fussball.model;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import active_record.ActiveRecord;

public class Match extends ActiveRecord {

	private String		annotation;
	private Long		attendace;

	private Set<Event>	events;

	private Stadium		stadium;
	private Team		teamA, teamB;

	private Set<Person>	startingLineUpTeamA, startingLineUpTeamB;

	private Long		startingTime;

	public Stadium getStadium() {
		return stadium;
	}

	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime.getTime();
	}

	public Date getStatingTime() {
		return new Date(startingTime);
	}
	
	public boolean addPlayerToLineUpTeamA(Person player) {
		return startingLineUpTeamA.add(player);
	}

	public boolean addPlayerToLineUpTeamA(Person player, Person... morePlayers) {
		return startingLineUpTeamA.add(player) | Collections.addAll(startingLineUpTeamB, morePlayers);
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
		return startingLineUpTeamB.add(player) | Collections.addAll(startingLineUpTeamA, morePlayers);
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
	


}
