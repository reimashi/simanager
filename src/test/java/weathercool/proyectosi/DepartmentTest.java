package weathercool.proyectosi;

import static org.junit.Assert.assertEquals;
import static weathercool.proyectosi.TransactionUtils.doTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.LazyInitializationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import weathercool.proyectosi.Department;
import weathercool.proyectosi.Employee;

public class DepartmentTest  extends SQLBasedTest {
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
	public void testCreateDepartment() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate(
				"INSERT INTO Employee(name) values('Daniel')", 
				Statement.RETURN_GENERATED_KEYS);
		
		Department d = new Department();
		d.setName("Budget");
		doTransaction(emf, em -> {
			em.persist(d);
			Employee e = em.find(Employee.class, id);
			e.setDeparment(d);
			
		});
		
		d.getEmployees();
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM Department WHERE id = "+d.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
		
		statement = jdbcConnection.createStatement();
		rs = statement.executeQuery(
				"SELECT department_id FROM Employee e WHERE id = "+id);
		rs.next();
		
		assertEquals(d.getId(), rs.getInt("department_id"));
	}
	
	//R
	@Test
	public void testFindDepartment() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Department(name) values('Projects')", 
				Statement.RETURN_GENERATED_KEYS);
		int deptId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Employee(name, department_id) values('Daniel', "+deptId+")", 
				Statement.RETURN_GENERATED_KEYS);
		int empId = getLastInsertedId(statement);
		
		Department d = emf.createEntityManager().find(Department.class, deptId);
		
		assertEquals(1, d.getEmployees().size());
		assertEquals(empId, d.getEmployees().iterator().next().getId());
		assertEquals(d, d.getEmployees().iterator().next().getDepartment());
	}
	
	private Department detachedDepartment = null;
	
	@Test(expected=LazyInitializationException.class)
	public void testLazyInitializationException() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Department(name) values('Budget')", 
				Statement.RETURN_GENERATED_KEYS);
		int deptId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Employee(name, department_id) values('Daniel', "+deptId+")", 
				Statement.RETURN_GENERATED_KEYS);
		int empId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Employee(name, department_id) values('Daniel', "+deptId+")", 
				Statement.RETURN_GENERATED_KEYS);
		
		doTransaction(emf, em -> {
			detachedDepartment = em.find(Department.class, deptId);
		});
		assertEquals(1, detachedDepartment.getEmployees().size());
		assertEquals(empId, detachedDepartment.getEmployees().iterator().next().getId());
		assertEquals(detachedDepartment, detachedDepartment.getEmployees().iterator().next().getDepartment());
	}
}
