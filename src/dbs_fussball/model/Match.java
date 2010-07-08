package dbs_fussball.model;

import java.util.Set;

import active_record.ActiveRecord;

public class Match extends ActiveRecord {

	private Long startingTime;
	
	private Long attendace;
	
	private String annotation;
	
	private Set<Event> events;
	
	private Stadium stadium;
	
	private Team teamA, teamB;
	
	private Set<Person> startingLineUpTeamA, startingLineUpTeamB;
	
}
