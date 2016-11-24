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
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(emf!=null && emf.isOpen()) emf.close();
	}
	
	//C
	@Test
	public void testCreateLogRecord() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate(
				"INSERT INTO LogRecord(table) values('test')",
				Statement.RETURN_GENERATED_KEYS);
		
		LogRecord d = new LogRecord();
		d.setAction("DELETE");
		doTransaction(emf, em -> {
			em.persist(d);
			User e = em.find(User.class, id);
			e.addLogRecord(d);
			
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
				"INSERT INTO User(name) values('Daniel')",
				Statement.RETURN_GENERATED_KEYS);
		int empId = getLastInsertedId(statement);

		statement.executeUpdate(
				"INSERT INTO LogRecord(table, user) values('test', " + empId + ")",
				Statement.RETURN_GENERATED_KEYS);
		int logId = getLastInsertedId(statement);
		
		LogRecord d = emf.createEntityManager().find(LogRecord.class, logId);
		
		assertEquals(d.getUser().getName(), "Daniel");
	}
	
	private LogRecord detachedLogRecord = null;
	
	@Test(expected=LazyInitializationException.class)
	public void testLazyInitializationException() throws SQLException {
		Statement statement = jdbcConnection.createStatement();

        statement = jdbcConnection.createStatement();
        statement.executeUpdate(
                "INSERT INTO User(name) values('Daniel')",
                Statement.RETURN_GENERATED_KEYS);
        int empId = getLastInsertedId(statement);

        statement.executeUpdate(
                "INSERT INTO LogRecord(table, user) values('test', " + empId + ")",
                Statement.RETURN_GENERATED_KEYS);
        int logId = getLastInsertedId(statement);

        statement = jdbcConnection.createStatement();
        statement.executeUpdate(
                "INSERT INTO LogRecord(table, user) values('test', " + empId + ")",
                Statement.RETURN_GENERATED_KEYS);
		
		doTransaction(emf, em -> {
			detachedLogRecord = em.find(LogRecord.class, logId);
		});

        assertEquals(detachedLogRecord.getUser().getName(), "Daniel");
	}
}
