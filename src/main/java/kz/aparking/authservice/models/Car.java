package kz.aparking.authservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mark")
    private String mark;

    @Column(name = "regist_number")
    @Pattern(regexp = "[A-Z]{0,2}[0-9]{2,3}[A-Z]{1,3}[0-9]{2}", message = "Invalid registration number format")
    private String registNumber;

    @JsonIgnore
    @JoinColumn(name = "user_id", nullable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Car(Long id, String mark, String registNumber, User user) {
        this.id = id;
        this.mark = mark;
        this.registNumber = registNumber;
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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getRegistNumber() {
        return registNumber;
    }

    public void setRegistNumber(String registNumber) {
        this.registNumber = registNumber;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
