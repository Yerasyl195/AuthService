package kz.aparking.authservice.services;

import kz.aparking.authservice.errors.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.jpa.UserOrderRepository;
import kz.aparking.authservice.jpa.UserRepository;
import kz.aparking.authservice.models.ParkingSession;
import kz.aparking.authservice.models.User;
import kz.aparking.authservice.services.UserService;
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
    public ParkingSession getCurrentSessionForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        ParkingSession lastSession = orderRepository.findTopByUserOrderByStartTimeDesc(user);
        if (lastSession.getEndTime() != null) {
            throw new RuntimeException("There is no active session for user with id: " + userId);
        }
        return lastSession;
    }

    @Override
    public ParkingSession createSessionForUser(Long userId, ParkingSession newOrder) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        newOrder.setUser(existingUser);
        return orderRepository.save(newOrder);
    }
}


