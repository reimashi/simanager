package weathercool.proyectosi;

import static org.junit.Assert.*;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

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
	
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.execute("DELETE FROM Measurement");

		super.renewConnection();
	}
	
	@Test
	public void testCreateMeasurement() throws SQLException {

		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO locationClass(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		int locId = getLastInsertedId(statement);
		
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
		
		statement.executeUpdate(
				"INSERT INTO Measurement(alert_id, location_id, time_id, elevation, temperature_surface, temperature_500mb,"
				+ "temperature_850mb, temperature_sea_level, cloud_cover_high, cloud_cover_half, cloud_cover_low,"
				+ "visibility, salinity, water_speed_eastward, water_speed_northward, wave_direction_mean,wave_period_absolute, wave_period_peak, wave_direction_peak,"
				+ "snow_level, snow_precipitation, rain_pecipitation, humidity, wind_direction, wind_lon, wind_lat, wind_gust) "
				+ "values("+alertId+","+locId+","+timeId+",0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)", 
				Statement.RETURN_GENERATED_KEYS);
		
		// check
		Statement statement2 = jdbcConnection.createStatement();
		ResultSet rs = statement2.executeQuery("SELECT COUNT(*) AS Total FROM Measurement WHERE alert_id = "+ alertId);
		rs.next();

		assertEquals(1, rs.getInt("total"));
	}
	
	@Test
	public void testFindMeasuresOfLocation() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO locationClass(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		int locId = getLastInsertedId(statement);
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
		LocationClass l = em.find(LocationClass.class, locId);
		
		List<Measurement> measures = em.createQuery("SELECT m "
				+ "FROM Measurement m WHERE location_id = :l", Measurement.class)
		.setParameter("l", l).getResultList();
		
		//check
		assertEquals(1, measures.size());
		assertEquals(alertId, measures.get(0).getAlert().getId());
	}
	
	@Test
	public void testDeleteMeasure() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO locationClass(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		int locId = getLastInsertedId(statement);
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
	

		statement = jdbcConnection.createStatement();
        statement.execute("DELETE FROM Measurement WHERE alert_id= "+ alertId +" & location_id="+ locId+" & time_id = "+ timeId);

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Measurement WHERE alert_id= "+ alertId +" & location_id="+ locId+" & time_id = "+ timeId);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}
	
	@Test
	public void testFindMeasuresOfAlert() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO locationClass(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		int locId = getLastInsertedId(statement);
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
		
		Statement statement2 = jdbcConnection.createStatement();
		statement2.executeUpdate(
				"INSERT INTO Measurement(alert_id, location_id, time_id, elevation, temperature_surface, temperature_500mb,"
				+ "temperature_850mb, temperature_sea_level, cloud_cover_high, cloud_cover_half, cloud_cover_low,"
				+ "visibility, salinity, water_speed_eastward, water_speed_northward, wave_direction_mean,wave_period_absolute, wave_period_peak, wave_direction_peak,"
				+ "snow_level, snow_precipitation, rain_pecipitation, humidity, wind_direction, wind_lon, wind_lat, wind_gust) "
				+ "values("+alertId+","+locId+","+timeId+",0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)", 
				Statement.RETURN_GENERATED_KEYS);


		EntityManager em = emf.createEntityManager();
		Alert a = em.find(Alert.class, alertId);
		
		List<Measurement> measures = em.createQuery("SELECT m "
				+ "FROM Measurement m WHERE alert_id = :a", Measurement.class)
		.setParameter("a", a).getResultList();

		//check
		assertEquals(1, measures.size());
		assertEquals(alertId, measures.get(0).getAlert().getId());
		assertEquals(timeId, measures.get(0).getTime().getId());
		assertEquals(locId, measures.get(0).getLocation().getId());
		
	}
	@Test
	public void testFindMeasuresOfTime() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO locationClass(latitude, longitude) values(94.132, 45.189)", 
				Statement.RETURN_GENERATED_KEYS);
		int locId = getLastInsertedId(statement);
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
		
		Statement statement2 = jdbcConnection.createStatement();
		statement2.executeUpdate(
				"INSERT INTO Measurement(alert_id, location_id, time_id, elevation, temperature_surface, temperature_500mb,"
				+ "temperature_850mb, temperature_sea_level, cloud_cover_high, cloud_cover_half, cloud_cover_low,"
				+ "visibility, salinity, water_speed_eastward, water_speed_northward, wave_direction_mean,wave_period_absolute, wave_period_peak, wave_direction_peak,"
				+ "snow_level, snow_precipitation, rain_pecipitation, humidity, wind_direction, wind_lon, wind_lat, wind_gust) "
				+ "values("+alertId+","+locId+","+timeId+",0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)", 
				Statement.RETURN_GENERATED_KEYS);


		EntityManager em = emf.createEntityManager();
		Time t = em.find(Time.class, timeId);
		
		List<Measurement> measures = em.createQuery("SELECT m "
				+ "FROM Measurement m WHERE time_id = :t", Measurement.class)
		.setParameter("t", t).getResultList();

		//check
		assertEquals(1, measures.size());
		assertEquals(alertId, measures.get(0).getAlert().getId());
		assertEquals(timeId, measures.get(0).getTime().getId());
		assertEquals(locId, measures.get(0).getLocation().getId());
		
	}
}