package weathercool.proyectosi;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

public class UserTest extends SQLBasedTest {
	private static EntityManagerFactory emf;

    static final String test_username = "usuariodeprueba";
    static final String test_password = "contrasenhadeprueba";
	
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
        statement.execute("DELETE FROM user WHERE username = '" + test_username + "'");

		super.renewConnection();
	}
	
	//C
	@Test
	public void testCreateUser() throws SQLException {
		final User emp = new User();
		
		doTransaction(emf, em -> {
		    emp.setUsername(test_username);
			emp.setPassword(test_password);
			em.persist(emp);
		});

        assertTrue(emp.checkPassword(test_password));
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total, password FROM user WHERE username = '" + emp.getUsername() + "'");
		rs.next();

        assertEquals(1, rs.getInt("total"));
        assertEquals(DigestUtils.sha1Hex(test_password), rs.getString("password"));
	}
	
	//R
	@Test
	public void testFindByUsername() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO user(username, name, password) values('" + test_username + "', '" + test_username + "', '" + test_password + "')");
		
		//test code
		User e = emf.createEntityManager().find(User.class, test_username);
		
		//assert code
		System.out.println("User of id " + test_username + " is: " + e);
		assertEquals(test_username, e.getName());
		assertEquals(test_username, e.getUsername());
	}
	
	//U
	@Test
	public void testUpdateUser() throws SQLException {
	    String test_name = "Usuario de prueba";

		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO user(username, name, password) values('" + test_username + "', NULL, '" + test_password + "')");
		
		doTransaction(emf, em -> {
			User e = em.find(User.class, test_username);
			e.setName(test_name);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM user WHERE username = '" + test_username + "'");
		rs.next();
		
		assertEquals(test_name, rs.getString("name"));
		assertEquals(test_username, rs.getString("username"));
	}
	
	//U
	private User aDetachedUser = null;
	@Test
	public void testUpdateByMerge() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO User(username, password) values('" + test_username + "', '" + test_password + "')");
		
		doTransaction(emf, em -> {
			aDetachedUser = em.find(User.class, test_username);
		});
		// e is detached, because the entitymanager em is closed (see doTransaction)
		
		aDetachedUser.setName("Daniel Glez");
		
		doTransaction(emf, em -> {
			em.merge(aDetachedUser);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM User WHERE username = '" + test_username + "'");
		rs.next();
		
		assertEquals("Daniel Glez", rs.getString("name"));
		assertEquals(test_username, rs.getString("username"));
	}
	
	//D
	@Test
	public void testDeleteUser() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO User(username, password) values('" + test_username + "', '" + test_password + "')");
		
		doTransaction(emf, em -> {
			User e = em.find(User.class, test_username);
			em.remove(e);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM User WHERE username = '" + test_username + "'");
		rs.next();
		
		assertEquals(0, rs.getInt("total"));
	}
	
	//L
	@Test
	public void testListUser() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO User(username, password) values('" + test_username + "1" + "', '" + test_password + "')");
		//prepare database for test
		statement.executeUpdate(
                "INSERT INTO User(username, password) values('" + test_username + "2" + "', '" + test_password + "')");
		
		List<User> users = emf.createEntityManager()
			.createQuery("SELECT u FROM User u ORDER BY u.username", User.class)
			.getResultList();

        statement.execute("DELETE FROM user WHERE username = '" + test_username + "1" + "'");
        statement.execute("DELETE FROM user WHERE username = '" + test_username + "2" + "'");
		
		//check (El usuario 0 es admin, creado por defecto)
		assertEquals(3, users.size());
		assertEquals(test_username + "1", users.get(1).getUsername());
		assertEquals(test_username + "2", users.get(2).getUsername());
	}
	
}