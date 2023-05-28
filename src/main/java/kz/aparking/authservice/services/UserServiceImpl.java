package kz.aparking.authservice.services;

import kz.aparking.authservice.dtos.RegistrationRequest;
import kz.aparking.authservice.errors.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kz.aparking.authservice.jpa.CarRepository;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.jpa.ParkingSessionRepository;
import kz.aparking.authservice.jpa.UserRepository;
import kz.aparking.authservice.models.Car;
import kz.aparking.authservice.models.ParkingSession;
import kz.aparking.authservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;
    private final UserRepository userRepository;

    private final ParkingSessionRepository parkingSessionRepository;

    private final CarService carService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ParkingSessionRepository parkingSessionRepository, CarService carService) {
        this.userRepository = userRepository;
        this.parkingSessionRepository = parkingSessionRepository;
        this.carService = carService;
    }

    @Override
    public User createUser(RegistrationRequest userDto) {
        if (userService.existsByPhone(userDto.getPhone())) {
            throw new RuntimeException("User with this phone number already exists");
        }
        if (userDto.cars == null) {
            throw new RuntimeException("Cars field can't be empty");
        }
        User newUser = new User();

        newUser.setFullName(userDto.getFullName());
        newUser.setPhone(userDto.getPhone());

        User savedUser = userRepository.save(newUser);

        for(Car car : userDto.getCars()) {
            carService.addCarForUser(savedUser.getId(), car);
        }

        return savedUser;
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public User updateUser(User updatedUser, Long id) {
        Optional<User> optionalUser =  userRepository.findById(id);

        if(optionalUser.isEmpty())
            throw new UserNotFoundException("can't find user with id:"+id);

        if (userService.existsByPhone(updatedUser.getPhone())) {
            throw new RuntimeException("User with this phone number already exists");
        }

        User newUser = optionalUser.get();
        newUser.setPhone(updatedUser.getPhone());
        newUser.setFullName(updatedUser.getFullName());

        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        userRepository.delete(existingUser);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User getCurrentUser() {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid or missing Authorization header");
        }
        jwtToken = jwtToken.substring(7);
        String phoneNumber = jwtTokenUtil.getPhoneNumberFromToken(jwtToken);
        return findByPhone(phoneNumber);
    }


    //history
    @Override
    public List<ParkingSession> getUserHistory(Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return existingUser.getParkingHistory();
    }

    @Override
    public ParkingSession createSessionForUser(Long userId, ParkingSession newOrder) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        boolean hasMatchingCar = existingUser.getCars().stream()
                .map(Car::getRegistNumber)
                .anyMatch(carNumber -> carNumber.equals(newOrder.getCarNumber()));

        if (!hasMatchingCar) {
            throw new RuntimeException("User does not have a car with number: " + newOrder.getCarNumber());
        }

        newOrder.setUser(existingUser);
        return parkingSessionRepository.save(newOrder);
    }

    @Override
    public ParkingSession getLastSessionForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        ParkingSession lastSession = parkingSessionRepository.findTopByUserOrderByStartTimeDesc(user);

        if (lastSession == null) {
            throw new RuntimeException("There are no parking sessions for user with id: " + userId);
        }
        return lastSession;
    }

    //car
    @Override
    public List<Car> getUserCars(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return user.getCars();
    }
}


