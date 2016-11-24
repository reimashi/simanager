package weathercool.proyectosi;

import javax.persistence.*;

@Entity
public class LogRecord {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String table;
	private String action;
	private String oldvalue;
	
	@ManyToMany
	private User user = null;
	
	public int getId() {
		return id;
	}

	public String getTable() {
		return this.table;
	}
	public void setTable(String name) {
		this.table = name;
	}

	public String getAction() {
		return this.action;
	}
	public void setAction(String name) {
		this.action = name;
	}

	public String getOldValue() {
		return this.oldvalue;
	}
	public void setOldValue(String oldvalue) {
		this.oldvalue = oldvalue;
	}

	public User getUser() {
		return this.user;
	}
    public void setUser(User user) {
        this.user = user;
    }
    public void internalSetUser(User user) {
        user.internalAddLogRecord(this);
        this.setUser(user);
    }
}
