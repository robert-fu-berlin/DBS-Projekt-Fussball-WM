package dbs_fussball;

import active_record.ActiveRecordMapper;
import dbs_fussball.model.Person;

public class Main {
	/**
	 * Just running some inital queries until we get proper tests
	 */
	
	public static void main(String[] args) {
		ActiveRecordMapper mapper = new ActiveRecordMapper("dbs_fussball", "postgres", "vuvuzela", "dbs");
		
		Person lukas = mapper.find(Person.class).where("firstName").is("Lukas").please();
		
		System.out.println(lukas.getStageName());
	}
}
