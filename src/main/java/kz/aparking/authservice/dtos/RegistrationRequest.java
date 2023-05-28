package kz.aparking.authservice.dtos;

import kz.aparking.authservice.models.Car;

import java.time.LocalDate;
import java.util.List;

public class RegistrationRequest {
    private String phone;
    private String fullName;
    public List<Car> cars;

    private LocalDate birthday;

    public RegistrationRequest(String phone, String fullName, List<Car> cars, String username, LocalDate birthday) {
        this.phone = phone;
        this.fullName = fullName;
        this.cars = cars;
        this.birthday = birthday;
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}