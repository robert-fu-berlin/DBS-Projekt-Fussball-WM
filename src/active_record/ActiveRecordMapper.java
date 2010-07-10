package active_record;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import active_record.finder.InitialMonoFinder;

public class ActiveRecordMapper {
	
	/** Credentials */
	private final String url, user, password; 

	private final String prefix;
	
	private Map<Class<? extends ActiveRecord>, ClassMapper<? extends ActiveRecord>> activeTables;
	
	public ActiveRecordMapper(String databaseName, String user, String password, String prefix) {
		this.url = "jdbc:postgresql:" + databaseName;
		this.user = user;
		this.password = password;
		this.prefix = prefix;
		
		this.activeTables = new HashMap<Class<? extends ActiveRecord>, ClassMapper<? extends ActiveRecord>>();
	}
	
	private <A extends ActiveRecord> void register(Class<A> activeRecord) {
		if (!activeTables.containsKey(activeRecord))
			activeTables.put(activeRecord, new ClassMapper<A>(activeRecord, this, prefix));
	}
	
	public <A extends ActiveRecord> A findBy(Class<A> activeRecordClass, Long id) throws SQLException {
		register(activeRecordClass);
				
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		
		Connection connection = DriverManager.getConnection(url, user, password);
		
		return (A) activeTables.get(activeRecordClass).findById(connection, id);
	}
	
	public <A extends ActiveRecord> InitialMonoFinder<A> find(Class<A> activeRecord) {
		register(activeRecord);
		
		ClassMapper<A> activeTable = (ClassMapper<A>) activeTables.get(activeRecord);
		return new ConcreteMonoFinder<A>(this, activeTable);
	}
	
	Connection obtainConnection() throws SQLException {
		Connection newConnection = DriverManager.getConnection(url, user, password);
		return newConnection;
	}

}
