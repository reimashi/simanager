package weathercool.proyectosi;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Project {
	@Id
	@GeneratedValue(strategy=IDENTITY)
	private int id;
	
	private String name;
	
	@ManyToMany
	private Set<Employee> employees = new HashSet<>();
	
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Employee> getEmployees() {
		return Collections.unmodifiableSet(employees);
	}

	public void addEmployee(Employee e) {
		e.internalAddProject(this);
		this.employees.add(e);
	}
	void internalAddEmployee(Employee employee) {
		this.employees.add(employee);
	}
	
	
	
}
