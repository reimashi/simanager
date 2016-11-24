import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MeasurementTest extends SQLBasedTest {
	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(emf!=null && emf.isOpen()) emf.close();
	}
	
	@Test
	public void testFindCoursesOfEmployee() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int locId = statement.executeUpdate(
				"INSERT INTO location(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Alert(temperature_high) values(20)", 
				Statement.RETURN_GENERATED_KEYS);
		int alertId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)", 
				Statement.RETURN_GENERATED_KEYS);
		int alertId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Measurement(idAlert, idTime, idLocation) "
				+ "values("+alertId+","+timeId+","+locId+")", 
				Statement.RETURN_GENERATED_KEYS);
		
		EntityManager em = emf.createEntityManager();
		Location l = em.find(Location.class, locId);
		
		List<Measurement> measures = em.createQuery("SELECT m.alert "
				+ "FROM Measurement m WHERE m.location = :l", Measurement.class)
		.setParameter("l", l).getResultList();
		
		//check
		assertEquals(1, measures.size());
		assertEquals("20", measures.get(0).getName());
	}
}