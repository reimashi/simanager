package weathercool.proyectosi;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

    private String username;
	private String name;
	private String password;

    @OneToMany(mappedBy="id")
    private Set<LogRecord> logs = new HashSet<>();

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setUsername(String name) {
        this.username = name;
    }
    public String getUsername() {
        return this.username;
    }

    public void setPassword(String pass) {
        this.password = Sha1(pass);
    }
    public String getPassword() {
        return this.password;
    }
    public boolean checkPassword(String pass) {
        return Sha1(pass) == this.password;
    }
	
	public int getId() {
		return id;
	}
	
	public Set<LogRecord> getLogRecords() {
		return Collections.unmodifiableSet(this.logs);
	}

    public void addLogRecord(LogRecord p) {
        this.logs.add(p);
    }

    public void internalAddLogRecord(LogRecord p) {
        p.internalSetUser(this);
        this.addLogRecord(p);
    }

	private static String Sha1(String src) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return new String(md.digest(src.getBytes()));
    }
}
