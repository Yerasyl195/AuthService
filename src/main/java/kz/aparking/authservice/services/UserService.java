package kz.aparking.authservice.services;

import kz.aparking.authservice.models.ParkingSession;
import kz.aparking.authservice.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    //User getUserByPhone(String phone);

    List<ParkingSession> getUserHistory(Long userId);

    ParkingSession createSessionForUser(Long userId, ParkingSession parkingSession);

    void deleteUser(Long userId);

    boolean existsByPhone(String phone);
    User findByPhone(String phone);
    User getCurrentUser();
}
