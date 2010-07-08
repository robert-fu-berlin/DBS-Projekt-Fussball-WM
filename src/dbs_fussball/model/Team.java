package dbs_fussball.model;

import java.util.Set;

import active_record.ActiveRecord;

public class Team extends ActiveRecord {

	private String name;
	
	private Set<Person> players;
	private Set<Person> associates;
	
	private Person trainer, assitantTrainer, doctor;
	
	private String nation;
}
	
