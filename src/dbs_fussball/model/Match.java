package dbs_fussball.model;

import java.util.Date;
import java.util.Set;

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

}
