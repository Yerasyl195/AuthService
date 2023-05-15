package kz.aparking.authservice.user;

import jakarta.servlet.http.HttpServletRequest;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.models.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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

    @GetMapping("/history/{id}")
    public List<UserOrder> GetParkingHistoryForUser(@PathVariable Long id) {
        return userService.getUserHistory(id);
    }

    @GetMapping("/history/{id}/last")
    public UserOrder getLastOrderForUser(@PathVariable Long id) {
        return userService.getLastOrderForUser(id);
    }

    @PostMapping("/history/{id}")
    public UserOrder addOrderToUserHistory(@PathVariable Long id, @RequestBody UserOrder newOrder) {
        return userService.addOrderToUserHistory(id, newOrder);
    }

    @GetMapping("/history/{id}/current")
    public UserOrder addCurrentSession(@PathVariable Long id) {
        return userService.getCurrentSession(id);
    }

    @GetMapping("/cars/{id}")
    public List<Car> getUserCars(@PathVariable Long userId) {
        return userService.getUserCars(userId);
    }
    @PostMapping("/cars/{id}")
    public Car addUserCar(@PathVariable Long id, @RequestBody Car newCar) {
        return userService.addUserCar(id, newCar);
    }
    @DeleteMapping("/cars/{id}")
    public void removeUserCar(@PathVariable Long id) {
        userService.removeUserCar(id);
    }
}


