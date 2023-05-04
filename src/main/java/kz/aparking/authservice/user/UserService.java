package kz.aparking.authservice.user;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    //User getUserByPhone(String phone);
    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long userId);

    boolean existsByPhone(String phone);
    User findByPhone(String phone);
    User getCurrentUser();
}
