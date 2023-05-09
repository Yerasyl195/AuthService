package kz.aparking.authservice.user;

import jakarta.servlet.http.HttpServletRequest;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.user.jpa.UserOrderRepository;
import kz.aparking.authservice.user.jpa.UserRepository;
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

//    @Override
//    public User getUserByPhone(String phone) {
//        Optional<User> optionalUser = userRepository.findByPhone(phone);
//        if (optionalUser.isPresent()) {
//            return optionalUser.get();
//        } else {
//            throw new UserNotFoundException("User with phone " + phone + " not found");
//        }
//    }

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
        existingUser.setCarNumbers(user.getCars());
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
}
//        String jwtToken = request.getHeader("Authorization").substring(7);
//        String phoneNumber = jwtTokenUtil.getPhoneNumberFromToken(jwtToken);
//        return findByPhone(phoneNumber);
//        String jwtToken = request.getHeader("Authorization");
//        if (jwtToken == null || jwtToken.isEmpty()) {
//            throw new RuntimeException("Authorization header is missing");
//        }
//        String phoneNumber = jwtToken.substring(7);
//        System.out.printf(phoneNumber);
//        return findByPhone(phoneNumber);


