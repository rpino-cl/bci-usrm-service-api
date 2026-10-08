package cl.bci.users.v1.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(exclude = "phones")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(name = "id", updatable = false, nullable = false, length = 36)
    private UUID id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 160)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;

    @Column(name = "modified", nullable = false)
    private LocalDateTime modified;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "token", length = 1000)
    private String token;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<Phone>();

    public User() {
        super();
    }

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void addPhone(Phone phone) {
        if (phone == null) {
            return;
        }
        this.phones.add(phone);
        phone.setUser(this);
    }

    public void removePhone(Phone phone) {
        if (phone == null) {
            return;
        }
        this.phones.remove(phone);
        phone.setUser(null);
    }

    @PrePersist
    void onCreate() {
    
        LocalDateTime now = LocalDateTime.now();
        if (this.created == null) {
            this.created = now;
        }
        if (this.modified == null) {
            this.modified = now;
        }
        if (this.lastLogin == null) {
            this.lastLogin = now;
        }
    }

    @PreUpdate
    void onUpdate() {
        this.modified = LocalDateTime.now();
    }

}
