package kz.aparking.authservice.jpa;

import kz.aparking.authservice.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository <Car, Long> {
}
