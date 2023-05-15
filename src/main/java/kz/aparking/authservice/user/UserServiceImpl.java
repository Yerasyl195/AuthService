package kz.aparking.authservice.user;

import jakarta.servlet.http.HttpServletRequest;
import kz.aparking.authservice.jpa.CarRepository;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.models.Car;
import kz.aparking.authservice.jpa.UserOrderRepository;
import kz.aparking.authservice.jpa.UserRepository;
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
    @Autowired
    private UserOrderRepository orderRepository;
    @Autowired
    private CarRepository carRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (userService.existsByPhone(user.getPhone())) {
            throw new RuntimeException("User with this phone number already exists");
        }
        return userRepository.save(user);
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
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getId()));
        existingUser.setPhone(user.getPhone());
        existingUser.setFullName(user.getFullName());
        userRepository.save(existingUser);
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


    @Override
    public List<UserOrder> getUserHistory(Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return existingUser.getParkingHistory();
    }

    @Override
    public UserOrder getLastOrderForUser(Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return orderRepository.findTopByUserOrderByStartTimeDesc(existingUser);
    }

    @Override
    public UserOrder addOrderToUserHistory(Long userId, UserOrder newOrder) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        newOrder.setUser(existingUser);
        return orderRepository.save(newOrder);
    }

    @Override
    public UserOrder getCurrentSession(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<UserOrder> history = user.getParkingHistory();

        if (history.isEmpty())
            throw new RuntimeException("parking history is empty for User with ID: " + userId.toString());

        UserOrder lastSession = history.get(history.size() - 1);
        if (lastSession.getEndTime() != null)
            throw new RuntimeException("no active session for User with ID: " + userId.toString());

        return lastSession;
    }

    @Override
    public Car addUserCar(Long userId, Car newCar) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        newCar.setUser(user);
        return carRepository.save(newCar);
    }

    @Override
    public List<Car> getUserCars(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return user.getCars();
    }

    @Override
    public void removeUserCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + carId));
        carRepository.delete(car);
    }


}


