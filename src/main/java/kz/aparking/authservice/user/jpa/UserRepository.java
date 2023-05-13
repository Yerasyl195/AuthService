package kz.aparking.authservice.user.jpa;

import kz.aparking.authservice.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    User findByPhone(String phone);

    //List<User> findAll();
}
