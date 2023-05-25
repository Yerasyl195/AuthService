package kz.aparking.authservice.services;

import kz.aparking.authservice.models.ParkingSession;
import org.springframework.stereotype.Service;

@Service
public interface ParkingSessionService {
    ParkingSession payParkingSession(Long id);
}


