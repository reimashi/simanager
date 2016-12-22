package weathercool.proyectosi;

import javax.persistence.*;
import java.util.Date;

@Entity
public class LogRecord {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String tableName;
	private String action;
    private String raw;
    private Date time;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		id = id;
	}

	public String getTableName() {
		return this.tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getAction() {
		return this.action;
	}
	public void setAction(String action) {
		this.action = action;
	}

    public String getRaw() {
        return this.raw;
    }
    public void setRaw(String raw) {
        this.raw = raw;
    }

    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }

	@ManyToOne
	private User user = null;

	public User getUser() {
		return this.user;
	}
    public void setUser(User user) {
        if (this.user != null) {
            this.user.internalRemoveLogRecord(this);
        }

        this.user = user;

        if (this.user != null) {
            this.user.internalAddLogRecord(this);
        }
    }
}
