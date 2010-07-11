package dbs_fussball.model;

import java.util.Set;

import active_record.ActiveRecord;

public class Team extends ActiveRecord {

	private String name;
	
	private Set<Person> players;
	private Set<Person> associates;
	
	private Person trainer, assitantTrainer, doctor;
	
	/** TODO change this to {@link FifaCountryCode} and handle it’s mapping to a postgres varchar(3) */ 
	private String nation;
}
	
