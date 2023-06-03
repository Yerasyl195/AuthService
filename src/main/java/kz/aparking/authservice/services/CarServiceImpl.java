package kz.aparking.authservice.services;

import jakarta.validation.Valid;
import kz.aparking.authservice.errors.UserNotFoundException;
import kz.aparking.authservice.jpa.CarRepository;
import kz.aparking.authservice.jpa.UserRepository;
import kz.aparking.authservice.models.Car;
import kz.aparking.authservice.models.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
        if (user.isEmpty()) {
            throw new UserNotFoundException("Can't find user with id: " + userId);
        }

        List<Car> userCars = user.get().getCars();

        boolean carExists = userCars.stream().anyMatch(c -> c.getRegistNumber().equals(car.getRegistNumber()));
        if (carExists) {
            throw new RuntimeException("Car with this registration number already exists");
        }

        car.setIsCurrentStatus(false);
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

    @Override
    public Car getCurrentCarForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        for(Car c : user.getCars()) {
            if(c.getIsCurrentStatus()) return c;
        }
        throw new UserNotFoundException("User with id " + userId + " doesn't have current car");
    }

    @Override
    public Car setCurrentCarForUser(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + carId));

        for (Car c : car.getUser().getCars()) {
            if (c.getIsCurrentStatus()) c.setIsCurrentStatus(false);
        }

        car.setIsCurrentStatus(true);
        return carRepository.save(car);
    }
}
