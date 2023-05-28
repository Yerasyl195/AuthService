package kz.aparking.authservice.services;

import jakarta.validation.Valid;
import kz.aparking.authservice.errors.UserNotFoundException;
import kz.aparking.authservice.jpa.CarRepository;
import kz.aparking.authservice.jpa.UserRepository;
import kz.aparking.authservice.models.Car;
import kz.aparking.authservice.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService{

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public CarServiceImpl(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car getCarById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        if(car.isEmpty())
            throw new RuntimeException("can't find car with id:"+id);
        return car.get();
    }

    @Override
    public Car addCarForUser(Long userId, @Valid Car car) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty())
            throw new UserNotFoundException("can't find user with id:"+userId);

        car.setUser(user.get());
        return carRepository.save(car);
    }

    @Override
    public void deleteCar(Long id) {
        Optional<Car> carOptional = carRepository.findById(id);

        if (carOptional.isEmpty()) {
            throw new RuntimeException("can't find Car with id:"+id);
        }
        carRepository.delete(carOptional.get());
    }
}
