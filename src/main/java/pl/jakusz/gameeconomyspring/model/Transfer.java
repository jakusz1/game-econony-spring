package pl.jakusz.gameeconomyspring.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "transfer")
public class Transfer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User minusUser;

    @ManyToOne
    private User plusUser;

    private int value;

    public Transfer(User minusUser, User plusUser, int value) {
        this.minusUser = minusUser;
        this.plusUser = plusUser;
        this.value = value;
    }

    public User getMinusUser() {
        return minusUser;
    }

    public User getPlusUser() {
        return plusUser;
    }

    public int getValue() {
        return value;
    }
}
