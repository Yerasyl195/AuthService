package kz.aparking.authservice.user.service;

import kz.aparking.authservice.user.models.User;
import kz.aparking.authservice.user.models.UserOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    //User getUserByPhone(String phone);

    List<UserOrder> getUserHistory(Long userId);
    UserOrder getLastOrderForUser(Long userId);
    UserOrder addOrderToUserHistory(Long userId, UserOrder userOrder);
    List<UserOrder> getUserHistoryForLast24Hours(Long userId);
    List<UserOrder> getAllUserHistoryForLast24Hours();

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long userId);

    boolean existsByPhone(String phone);
    User findByPhone(String phone);
    User getCurrentUser();


}
