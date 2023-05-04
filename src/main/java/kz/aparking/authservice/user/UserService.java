package kz.aparking.authservice.user;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    User getUserByPhone(String phone);
    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long userId);

    boolean existsByPhoneNumber(String phoneNumber);
    User findByPhoneNumber(String phoneNumber);
}
