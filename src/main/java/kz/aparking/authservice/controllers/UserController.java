package kz.aparking.authservice.controllers;

import kz.aparking.authservice.errors.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.models.Car;
import kz.aparking.authservice.models.ParkingSession;
import kz.aparking.authservice.models.User;
import kz.aparking.authservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final HttpServletRequest request;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil, HttpServletRequest request) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.request = request;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/byPhone")
    public ResponseEntity<User> getUserByPhone(@RequestParam String phone) {
        User user = userService.findByPhone(phone);
        if (user == null) {
            throw new UserNotFoundException("User with phone " + phone + " not found");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        try {
            String jwtToken = request.getHeader("Authorization");
            if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
                throw new RuntimeException("Invalid or missing Authorization header");
            }
            jwtToken = jwtToken.substring(7);
            String phoneNumber = jwtTokenUtil.getPhoneNumberFromToken(jwtToken);
            User currentUser = userService.findByPhone(phoneNumber);
            if (currentUser == null) {
                throw new UserNotFoundException("User with phone not found");
            }
            return ResponseEntity.ok(currentUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }


    //history
    @PostMapping("/history/{id}")
    public ParkingSession createSessionForUser(@PathVariable Long id, @RequestBody ParkingSession newOrder) {
        return userService.createSessionForUser(id, newOrder);
    }

    @GetMapping("/history/{id}")
    public List<ParkingSession> getParkingHistoryForUser(@PathVariable Long id) {
        return userService.getUserHistory(id);
    }

    @GetMapping("/cars/{id}")
    public List<Car> getUserCars(@PathVariable Long id) {
        return userService.getUserCars(id);
    }

//    @GetMapping("/history/{id}/last")
//    public ParkingSession getCurrentSessionForUser(@PathVariable Long id) {
//        return userService.getCurrentSessionForUser(id);
//    }

}


