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

import weathercool.proyectosi.Course;
import weathercool.proyectosi.Employee;

public class CourseEnrolmentTest extends SQLBasedTest {
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
		int empId = statement.executeUpdate(
				"INSERT INTO Employee(name) values('Daniel')", 
				Statement.RETURN_GENERATED_KEYS);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO Course(name) values('.NET')", 
				Statement.RETURN_GENERATED_KEYS);
		int courseId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO CourseEnrolment(course_id, employee_id, date) "
				+ "values("+courseId+","+empId+",'2016-11-17')", 
				Statement.RETURN_GENERATED_KEYS);
		
		EntityManager em = emf.createEntityManager();
		Employee e = em.find(Employee.class, empId);
		
		List<Course> courses = em.createQuery("SELECT enrolment.course "
				+ "FROM CourseEnrolment enrolment WHERE enrolment.employee = :e", Course.class)
		.setParameter("e", e).getResultList();
		
		//check
		assertEquals(1, courses.size());
		assertEquals(".NET", courses.get(0).getName());
	}
}
