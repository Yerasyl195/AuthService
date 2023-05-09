package kz.aparking.authservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name="user_parking_record")
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
    private String parkingSpotNumber;
    @Column(nullable = false)
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String price;
    private boolean isPaid;
    private String carNumber;

    public UserOrder(String parkingSpotNumber, LocalDateTime startTime, LocalDateTime endTime, String price, boolean isPaid, String carNumber) {
        this.parkingSpotNumber = parkingSpotNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.isPaid = isPaid;
        this.carNumber = carNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }


    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    protected UserOrder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getParkingSpotNumber() {
        return parkingSpotNumber;
    }

    public void setParkingSpotNumber(String parkingSpot) {
        this.parkingSpotNumber = parkingSpot;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}

