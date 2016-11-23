package weathercool.proyectosi;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Department {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@OneToMany(mappedBy="department")
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

	public void addEmployee(Employee employee) {
		employee.setDeparment(this);
	}
	
	public void removeEmployee(Employee employee) {
		employee.setDeparment(null);
	}
	
	void internalRemoveEmployee(Employee employee) {
		this.employees.remove(employee);
	}

	void internalAddEmployee(Employee employee) {
		this.employees.add(employee);
	}
	
}
