package pl.jakusz.gameeconomyspring.model;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    public static int START_BALANCE = 15000;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int balance;

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
