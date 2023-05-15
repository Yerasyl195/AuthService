package kz.aparking.authservice.jpa;
import kz.aparking.authservice.user.User;
import kz.aparking.authservice.user.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {
    UserOrder findTopByUserOrderByStartTimeDesc(User user);
}

