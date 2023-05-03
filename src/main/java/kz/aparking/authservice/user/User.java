package kz.aparking.authservice.user;


import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "full_name")
    private String fullName;

    @ElementCollection
    @CollectionTable(name = "cars", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "car")
    private List<String> cars;

    //private List<ParkingHistory> parkingHistory;

    public Long getId() {
        return id;
    }

//    public List<ParkingHistory> getParkingHistory() {
//        return parkingHistory;
//    }

    public String getPhone() {
        return phone;
    }

    public List<String> getCars() {
        return cars;
    }

    public String getFullName() {
        return fullName;
    }

    //    public void setParkingHistory(List<ParkingHistory> parkingHistory) {
//        this.parkingHistory = parkingHistory;
//    }
    public User(Long id, String phone, String fullName, List<String> cars) {//, List<ParkingHistory> parkingHistory) {
        this.id = id;
        this.phone = phone;
        this.fullName = fullName;
        this.cars = cars;
        //this.parkingHistory = parkingHistory;
    }

    public User() {
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCarNumbers(List<String> cars) {
        this.cars = cars;
    }
}



