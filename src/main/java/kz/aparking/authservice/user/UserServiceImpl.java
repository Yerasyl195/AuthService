package kz.aparking.authservice.user;

import jakarta.servlet.http.HttpServletRequest;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
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
    public User getUserByPhone(String phone) {
        Optional<User> optionalUser = userRepository.findByPhone(phone);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserNotFoundException("User with phone " + phone + " not found");
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
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
    @Override
    public User getCurrentUser() {
        String jwtToken = request.getHeader("Authorization").substring(7);
        String phoneNumber = jwtTokenUtil.getPhoneNumberFromToken(jwtToken);
        return findByPhoneNumber(phoneNumber);
    }
}


