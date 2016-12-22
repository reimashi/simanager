package weathercool.proyectosi;

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
	public void testFindMeasures() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int locId = statement.executeUpdate(
				"INSERT INTO location(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO alert(temperature_high, temperature_half, temperature_low, rain_high, rain_half, rain_low) values(15, 25, 27, 30, 1, 23)",
				Statement.RETURN_GENERATED_KEYS);

		int alertId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO time(year, month, day, hour, minute, second) values(2016, 11, 20, 17, 55, 38)", 
				Statement.RETURN_GENERATED_KEYS);

		int timeId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Measurement(alert_id, location_id, time_id, elevation, temperature_surface, temperature_500mb,"
				+ "temperature_850mb, temperature_sea_level, cloud_cover_high, cloud_cover_half, cloud_cover_low,"
				+ "visibility, salinity, water_speed_eastward, water_speed_northward, wave_direction_mean,wave_period_absolute, wave_period_peak, wave_direction_peak,"
				+ "snow_level, snow_precipitation, rain_pecipitation, humidity, wind_direction, wind_lon, wind_lat, wind_gust) "
				+ "values("+alertId+","+locId+","+timeId+",0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)", 
				Statement.RETURN_GENERATED_KEYS);
		
	
		EntityManager em = emf.createEntityManager();
		Location l = em.find(Location.class, locId);
		
		List<Measurement> measures = em.createQuery("SELECT m "
				+ "FROM Measurement m WHERE location_id = :l", Measurement.class)
		.setParameter("l", l).getResultList();
		
		//check
		assertEquals(1, measures.size());
		assertEquals(alertId, measures.get(0).getAlert());
	}
}