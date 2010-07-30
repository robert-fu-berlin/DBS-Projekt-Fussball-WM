package dbs_fussball;

import java.util.ArrayList;
import java.util.List;

import active_record.ActiveRecord;
import active_record.ActiveRecordMapper;
import dbs_fussball.model.Cup;
import dbs_fussball.model.Event;
import dbs_fussball.model.Match;
import dbs_fussball.model.Person;
import dbs_fussball.model.Stadium;
import dbs_fussball.model.Team;
import dbs_fussball.model.User;
import dbs_fussball.model.Usergroup;

public class Main {

	public static void main(String[] args) {
		List<Class<? extends ActiveRecord>> classes = new ArrayList<Class<? extends ActiveRecord>>();
		classes.add(Cup.class);
		classes.add(Event.class);
		classes.add(Match.class);
		classes.add(Person.class);
		classes.add(Stadium.class);
		classes.add(Team.class);
		classes.add(User.class);
		classes.add(Usergroup.class);

		ActiveRecordMapper arm = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "dbs");
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
	}
}
