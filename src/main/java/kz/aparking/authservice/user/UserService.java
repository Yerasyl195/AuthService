package kz.aparking.authservice.user;

import kz.aparking.authservice.models.Car;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User createUser(User user);
    User getUserById(Long id);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Long userId);

    boolean existsByPhone(String phone);
    User findByPhone(String phone);
    User getCurrentUser();

    List<UserOrder> getUserHistory(Long userId);
    UserOrder getLastOrderForUser(Long userId);
    UserOrder addOrderToUserHistory(Long userId, UserOrder userOrder);
    UserOrder getCurrentSession(Long userId);
    Car addUserCar(Long UserId, Car car);
    List<Car> getUserCars(Long UserId);
    void removeUserCar(Long CarId);
}
