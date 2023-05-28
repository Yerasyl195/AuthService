package kz.aparking.authservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.aparking.authservice.models.Car;
import kz.aparking.authservice.models.ParkingSession;

import java.util.List;

public class RegistrationRequest {
    private String phone;
    private String fullName;
    public List<Car> cars;

    public RegistrationRequest(String phone, String fullName, List<Car> cars, String username) {
        this.phone = phone;
        this.fullName = fullName;
        this.cars = cars;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}