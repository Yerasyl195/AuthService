package kz.aparking.authservice.controllers;

import kz.aparking.authservice.models.ParkingSession;
import kz.aparking.authservice.services.ParkingSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/history")
public class ParkingSessionController {
    private final ParkingSessionService parkingSessionService;

    public ParkingSessionController(ParkingSessionService parkingSessionService) {
        this.parkingSessionService = parkingSessionService;
    }

    @PutMapping("/pay/{id}")
    public ResponseEntity<Object> payParkingSession(@PathVariable Long id) {
        ParkingSession session = parkingSessionService.payParkingSession(id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(session.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
