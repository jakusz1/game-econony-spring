package pl.jakusz.gameeconomyspring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "player")
public class User {

    public static int START_BALANCE = 15000;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private int balance;

    protected User(){

    }

    public User(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }
    public User(String name) {
        this.name = name;
        this.balance = START_BALANCE;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int changeBalance(int value){
        return this.balance+=value;
    }
}
