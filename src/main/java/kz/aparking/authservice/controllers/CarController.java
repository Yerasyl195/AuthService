package kz.aparking.authservice.controllers;

import kz.aparking.authservice.models.Car;
import kz.aparking.authservice.services.CarServiceImpl;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/car")
public class CarController {
    private final CarServiceImpl carService;

    public CarController(CarServiceImpl carService) {
        this.carService = carService;
    }

    @GetMapping("")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public EntityModel<Car> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);

        EntityModel<Car> entityModel = EntityModel.of(car);
        WebMvcLinkBuilder link =  linkTo(methodOn(this.getClass()).getAllCars());
        entityModel.add(link.withRel("all-cars"));

        return entityModel;
    }

    @PostMapping("user/id")
    public ResponseEntity<Car> addCarForUser(@PathVariable Long userId, @RequestBody Car car) {
        Car savedCar = carService.addCarForUser(userId, car);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCar.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

}
