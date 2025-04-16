package entities;

import entities.enums.Roles;

import java.io.Serializable;


public class User {
        private int id;
        private String name;
        private String lastName;
        private String password;
        private Roles role;
        private int balance;

    public User(int id, String name, String lastName, String password, Roles role, int balance) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.balance = balance;
    }


    public User() {
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                ", balance=" + balance +
                '}';
    }
}
