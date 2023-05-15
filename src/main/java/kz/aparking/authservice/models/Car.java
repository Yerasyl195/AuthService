package kz.aparking.authservice.models;

import jakarta.persistence.*;
import kz.aparking.authservice.user.User;

@Entity
public class Car {
    @Id
    @GeneratedValue
    private Long id;
    private String brand;
    private String number;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Car(Long id, String brand, String number, User user) {
        this.id = id;
        this.brand = brand;
        this.number = number;
        this.user = user;
    }

    public Car() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
