package dbs_fussball.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import active_record.ActiveRecord;

import com.google.common.base.Preconditions;
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
		Preconditions.checkNotNull(cup);
		events = new HashSet<Event>();
		this.cup = cup;
	}

	public Match(Cup cup, Team teamA, Team teamB) {
		this(cup);
		Preconditions.checkNotNull(teamA);
		Preconditions.checkNotNull(teamB);
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
		Preconditions.checkNotNull(startingTime);

		this.startingTime = startingTime.getTime();
	}

	public boolean addPlayerToLineUpTeamA(Person player) {
		Preconditions.checkNotNull(player);
		Preconditions.checkArgument(teamA.containsPlayer(player), "Player must be part of team.");

		return startingLineUpTeamA.add(player);
	}

	public boolean addPlayerToLineUpTeamA(Person player, Person... morePlayers) {
		return startingLineUpTeamA.add(player) | Collections.addAll(startingLineUpTeamA, morePlayers);
	}

	public boolean lineUpTeamAContains(Person player) {
		return startingLineUpTeamA.contains(player);
	}

	public boolean removePlayerFromLineUpTeamA(Person player) {
		Preconditions.checkNotNull(player);
		Preconditions.checkArgument(teamA.containsPlayer(player), "Player must be part of team.");

		Set<Event> eventsToRemove = new HashSet<Event>();
		for (Event e : events)
			if (player.equals(e.getPrimary()) || player.equals(e.getSecondary()))
				eventsToRemove.add(e);
		for (Event e : eventsToRemove)
			events.remove(e);

		return startingLineUpTeamA.remove(player);
	}

	public int numberOfPlayersInLineUpTeamA() {
		return startingLineUpTeamA.size();
	}

	public int numberOfPlayersInLineUpTeamB() {
		return startingLineUpTeamB.size();
	}

	public Iterable<Person> playersOfLineUpTeamA() {
		return Iterables.unmodifiableIterable(startingLineUpTeamA);
	}

	public Iterator<Person> playersOfLineUpTeamAIterator() {
		return Iterators.unmodifiableIterator(startingLineUpTeamA.iterator());
	}

	public boolean addPlayerToLineUpTeamB(Person player) {
		Preconditions.checkNotNull(player);
		Preconditions.checkArgument(teamB.containsPlayer(player), "Player must be part of team.");

		return startingLineUpTeamB.add(player);
	}

	public boolean addPlayerToLineUpTeamB(Person player, Person... morePlayers) {
		return startingLineUpTeamB.add(player) | Collections.addAll(startingLineUpTeamB, morePlayers);
	}

	public boolean lineUpTeamBContains(Person player) {
		return startingLineUpTeamB.contains(player);
	}

	public boolean removePlayerFromLineUpTeamB(Person player) {
		Preconditions.checkNotNull(player);
		Preconditions.checkArgument(teamB.containsPlayer(player), "Player must be part of team.");

		for (Event e : events) {
			if (e.getPrimary() != null)
				Preconditions
						.checkArgument(!player.equals(e.getPrimary()), "Player must not be referenced by an Event");
			if (e.getSecondary() != null)
				Preconditions.checkArgument(!player.equals(e.getSecondary()),
						"Player must not be referenced by an Event");
		}

		return startingLineUpTeamB.remove(player);
	}

	public Iterable<Person> playersOfLineUpTeamB() {
		return Iterables.unmodifiableIterable(startingLineUpTeamB);
	}

	public Iterator<Person> playersOfLineUpTeamBIterator() {
		return Iterators.unmodifiableIterator(startingLineUpTeamB.iterator());
	}
	public boolean addEvent(Event event) {
		switch (event.getType()) {
			case EXCHANGE:
				Preconditions.checkArgument(!isOnField(event.getSecondary(), event.getTime()),
						"Player must be on the field");
			case GOAL:
			case PENALTY_GOAL:
			case OWN_GOAL:
			case YELLOW_CARD:
			case RED_CARD:
			case FOUL:
				Preconditions.checkArgument(isOnField(event.getPrimary(), event.getTime()),
						"Player must be on the field");
				break;
		}

		return events.add(event);
	}

	private boolean isOnField(Person person, Float time) {
		Preconditions.checkNotNull(person);
		Preconditions.checkNotNull(time);

		Float lowerBound = null;
		if (startingLineUpTeamA.contains(person) || startingLineUpTeamB.contains(person))
			lowerBound = 0.0f;
		else
			for (Event e : events)
				if (e.getType() == Event.Type.EXCHANGE && person.equals(e.getSecondary())) {
					lowerBound = e.getTime();
					break;
				}

		if (lowerBound == null)
			return false;

		Float upperBound = null;
		loop: for (Event e : events)
			switch (e.getType()) {
				case RED_CARD:
				case EXCHANGE:
					if (person.equals(e.getPrimary()))
						break loop;
			}

		if (upperBound == null)
			upperBound = Float.POSITIVE_INFINITY;

		return lowerBound <= time && time < upperBound;
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
		Preconditions.checkNotNull(attendace);
		Preconditions.checkArgument(attendace > 0);

		this.attendace = attendace;
	}

	public Integer getAttendace() {
		return attendace;
	}

	public void setAnnotation(String annotation) {
		Preconditions.checkNotNull(annotation);

		this.annotation = annotation;
	}

	public String getAnnotation() {
		return annotation;
	}

	public int getGoalsA() {
		int goals = 0;
		for (Event e : events)
			switch (e.getType()) {
				case GOAL:
				case PENALTY_GOAL:
					if (teamA.containsPlayer(e.getPrimary()))
						goals++;
					break;
				case OWN_GOAL:
					if (teamB.containsPlayer(e.getPrimary()))
						goals++;
					break;
			}
		return goals;
	}

	public int getGoalsB() {
		int goals = 0;
		for (Event e : events)
			switch (e.getType()) {
				case GOAL:
				case PENALTY_GOAL:
					if (teamB.containsPlayer(e.getPrimary()))
						goals++;
					break;
				case OWN_GOAL:
					if (teamA.containsPlayer(e.getPrimary()))
						goals++;
					break;
			}
		return goals;
	}

	public boolean isOver() {
		for (Event e : events)
			if (e.getType() == Event.Type.END)
				return true;
		return false;
	}
}
