package kz.aparking.authservice.services;

import kz.aparking.authservice.dtos.RegistrationRequest;
import kz.aparking.authservice.models.Car;
import kz.aparking.authservice.models.ParkingSession;
import kz.aparking.authservice.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User createUser(RegistrationRequest userDto);
    User getUserById(Long id);

    User updateUser(User updatedUser, Long id);
    void deleteUser(Long userId);
    boolean existsByPhone(String phone);
    User findByPhone(String phone);
    User getCurrentUser();
    List<ParkingSession> getUserHistory(Long userId);

    ParkingSession createSessionForUser(Long userId, ParkingSession parkingSession);

    ParkingSession getLastSessionForUser(Long UserId);
    List<Car> getUserCars(Long userId);

}
