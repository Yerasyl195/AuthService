package kz.aparking.authservice.jpa;
import kz.aparking.authservice.models.User;
import kz.aparking.authservice.models.ParkingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<ParkingSession, Long> {
    ParkingSession findTopByUserOrderByStartTimeDesc(User user);
}

