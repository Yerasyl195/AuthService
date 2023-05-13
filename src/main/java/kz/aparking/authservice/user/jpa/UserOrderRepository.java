package kz.aparking.authservice.user.jpa;
import kz.aparking.authservice.user.models.User;
import kz.aparking.authservice.user.models.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {
    UserOrder findTopByUserOrderByStartTimeDesc(User user);
    //List<UserOrder> findAllByEndTimeDateEquals(LocalDateTime date);
    @Query("SELECT uo FROM UserOrder uo WHERE DATE(uo.endTime) = :date")
    List<UserOrder> findAllByEndTimeDate(@Param("date") LocalDate date);
}

