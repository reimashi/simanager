package weathercool.proyectosi;

import org.hibernate.LazyInitializationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

public class LogRecordTest extends SQLBasedTest {
	private static EntityManagerFactory emf;

	static final String test_username = "usuariodeprueba";
	static final String test_password = "contrasenhadeprueba";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");

		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO User(username, password) values('" + test_username + "', '" + test_password + "')");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Statement statement = jdbcConnection.createStatement();
		statement.execute("DELETE FROM LogRecord");
		statement.execute("DELETE FROM user WHERE username = '" + test_username + "'");

		if(emf!=null && emf.isOpen()) emf.close();
	}
	
	//C
	@Test
	public void testCreateLogRecord() throws SQLException {
		Statement statement = jdbcConnection.createStatement();

		int id = statement.executeUpdate(
				"INSERT INTO LogRecord(tableName, user_username, action) values ('time', '" + test_username + "', 'delete')",
				Statement.RETURN_GENERATED_KEYS);
		
		LogRecord d = new LogRecord();
		d.setAction("DELETE");
		doTransaction(emf, em -> {
			em.persist(d);
			User e = em.find(User.class, test_username);
		});
		
		d.getUser();
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM LogRecord WHERE id = "+d.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
	}
	
	//R
	@Test
	public void testFindLogRecord() throws SQLException {
		Statement statement = jdbcConnection.createStatement();

		statement = jdbcConnection.createStatement();

		statement.executeUpdate(
				"INSERT INTO LogRecord(tableName, user_username, action) values ('time', '" + test_username + "', 'delete')",
				Statement.RETURN_GENERATED_KEYS);
		int logId = getLastInsertedId(statement);
		
		LogRecord d = emf.createEntityManager().find(LogRecord.class, logId);
		
		assertEquals(d.getUser().getUsername(), test_username);
	}
}
