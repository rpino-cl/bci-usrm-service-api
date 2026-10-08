package cl.bci.users.v1.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "phones")
@Getter
@Setter
@ToString(exclude = "user")
public class Phone {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(name = "id", updatable = false, nullable = false, length = 36)
    private UUID id;

    @Column(name = "number", nullable = false, length = 30)
    private String number;

    @Column(name = "citycode", length = 10)
    private String citycode;
    
    @Column(name = "contrycode", length = 10)
    private String contrycode;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Phone() {
        super();
    }

    public Phone(String number, String citycode, String contrycode) {
        super();
        this.number = number;
        this.citycode = citycode;
        this.contrycode = contrycode;
    }

}
