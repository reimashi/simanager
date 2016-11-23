package weathercool.proyectosi;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(CourseEnrolment.CourseEnrolmentKey.class)
public class CourseEnrolment {

	@Id
	@ManyToOne
	private Course course;
	
	@Id
	@ManyToOne
	private Employee employee;
	
	private Date date;
	
	public static class CourseEnrolmentKey
		implements Serializable {
		
		private int course;
		
		private int employee;
		
		public CourseEnrolmentKey() {
			// TODO Auto-generated constructor stub
		}

		public CourseEnrolmentKey(int course, int employee) {
			super();
			this.course = course;
			this.employee = employee;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + course;
			result = prime * result + employee;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CourseEnrolmentKey other = (CourseEnrolmentKey) obj;
			if (course != other.course)
				return false;
			if (employee != other.employee)
				return false;
			return true;
		}
	}
}
