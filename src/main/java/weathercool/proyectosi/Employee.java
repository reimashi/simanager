package weathercool.proyectosi;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@ManyToOne
	private Department department;
	
	@ManyToMany(mappedBy="employees")
	private Set<Project> projects = new HashSet<>();
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public void setDeparment(Department d) {
		if (this.department != null) {
			this.department.internalRemoveEmployee(this);
		}
		
		this.department = d;
		
		if (this.department != null) {
			this.department.internalAddEmployee(this);
		}
	}

	public Department getDepartment() {
		return this.department;
	}
	
	public Set<Project> getProjects() {
		return Collections.unmodifiableSet(this.projects);
	}
	
	public void addProject(Project p) {
		p.internalAddEmployee(this);
		this.projects.add(p);
	}

	public void internalAddProject(Project project) {
		this.projects.add(project);
	}

}
