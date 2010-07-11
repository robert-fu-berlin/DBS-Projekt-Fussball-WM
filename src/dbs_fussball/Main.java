package dbs_fussball;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import active_record.ActiveRecord;
import active_record.ActiveRecordMapper;

import dbs_fussball.model.*;

public class Main {
	/**
	 * Just running some inital queries until we get proper tests
	 */

	public static void main(String[] args) {
		List<Class<? extends ActiveRecord>> classes = new ArrayList<Class<? extends ActiveRecord>>();
		classes.add(Cup.class);
		classes.add(Event.class);
		classes.add(Match.class);
		classes.add(Person.class);
		classes.add(Stadium.class);
		classes.add(Team.class);

		ActiveRecordMapper arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvzela", "dbs");
		// Setup tables
		for (Class<? extends ActiveRecord> activeRecord : classes) {
			// Drop existing tables
			try {
				arm.dropTable(activeRecord);
				System.out.println("Dropped table for " + activeRecord.getSimpleName());
			} catch (Exception e) {
				System.err.println("Could not drop table for " + activeRecord.getSimpleName());
				System.err.println(e.getLocalizedMessage());
			}
			// Create new table
			try {
				arm.createTable(activeRecord);
				System.out.println("Created table for " + activeRecord.getSimpleName());
			} catch (Exception e) {
				System.err.println("Could not create table for " + activeRecord.getSimpleName());
				System.err.println(e.getLocalizedMessage());
			}
		}
		
		Person lukas = new Person("Lukas", "Podolski");
		lukas.setStageName("Poldi");
		
		try {
			arm.save(lukas);
		} catch (SQLException e) {
			System.err.println("Could not save " + lukas.getLastName());
		}
		
		Person maybeLukas = arm.find(Person.class).where("firstName").is("Lukas").please();
		
		System.out.println(maybeLukas.getFirstName() + " " + maybeLukas.getLastName());
	}
}
