package kz.aparking.authservice.services;

import kz.aparking.authservice.models.Car;

import java.util.List;

public interface CarService {
    public List<Car> getAllCars();
    public Car getCarById(Long id);
    public Car addCarForUser(Long userId, Car car);
    public void deleteCar(Long id);
    Car getCurrentCarForUser(Long userId);
    Car setCurrentCarForUser(Long carId);
}
