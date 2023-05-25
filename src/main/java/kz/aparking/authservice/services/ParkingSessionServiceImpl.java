package kz.aparking.authservice.services;
import kz.aparking.authservice.jpa.ParkingSessionRepository;
import kz.aparking.authservice.models.ParkingSession;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ParkingSessionServiceImpl implements ParkingSessionService{
    private final ParkingSessionRepository parkingSessionRepository;

    public ParkingSessionServiceImpl(ParkingSessionRepository parkingSessionRepository) {
        this.parkingSessionRepository = parkingSessionRepository;
    }

    public ParkingSession payParkingSession(Long id) {
        Optional<ParkingSession> session = parkingSessionRepository.findById(id);
        if(session.isEmpty())
            throw new RuntimeException("can't find Parking Session with id:"+id);

        ParkingSession updatedSession = session.get();
        updatedSession.setPaid(true);
        return parkingSessionRepository.save(updatedSession);
    }
}
