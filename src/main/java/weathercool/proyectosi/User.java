package weathercool.proyectosi;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    public User() {}
    public User(String username) {
        this.username = username;
    }

    @Id
    private String username;

    @Column(nullable = true)
	private String name;

    @Column
	private String password;

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
        this.password = DigestUtils.sha1Hex(pass);
    }
    public String getPassword() {
        return this.password;
    }
    public boolean checkPassword(String pass) {
        return DigestUtils.sha1Hex(pass).equals(this.password);
    }

    @OneToMany(mappedBy="user")
    private Set<LogRecord> logs = new HashSet<LogRecord>();

    public Set<LogRecord> getLogRecords() {
        return Collections.unmodifiableSet(this.logs);
    }

    public void addLogRecord(LogRecord p) {
        p.setUser(this);
    }

    public void removeLogRecord(LogRecord p) {
        p.setUser(null);
    }

    public void internalAddLogRecord(LogRecord p) {
        this.logs.add(p);
    }

    public void internalRemoveLogRecord(LogRecord p) {
        this.logs.remove(p);
    }
}
