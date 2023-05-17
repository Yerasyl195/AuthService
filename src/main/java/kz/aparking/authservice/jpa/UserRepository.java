package kz.aparking.authservice.jpa;

import kz.aparking.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    User findByPhone(String phone);
}
