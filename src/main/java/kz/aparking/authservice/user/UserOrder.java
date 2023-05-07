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

    private String sensorId;
    private double price;
    private boolean isPaid;

    public UserOrder(String parkingSpotNumber, LocalDateTime startTime, LocalDateTime endTime, String sensorId, double price, boolean isPaid) {
        this.parkingSpotNumber = parkingSpotNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sensorId = sensorId;
        this.price = price;
        this.isPaid = isPaid;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double cost) {
        this.price = cost;
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

