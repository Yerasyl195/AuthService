package kz.aparking.authservice.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "full_name")
    private String fullName;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingSession> parkingHistory;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars;

    public User(Long id, String phone, String fullName, List<String> cars) {//, List<ParkingHistory> parkingHistory) {
        this.id = id;
        this.phone = phone;
        this.fullName = fullName;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public List<Car> getCars() {
        return cars;
    }

    public String getFullName() {
        return fullName;
    }


    public List<ParkingSession> getParkingHistory() {
        return parkingHistory;
    }

    public void setParkingHistory(List<ParkingSession> parkingHistory) {
        this.parkingHistory = parkingHistory;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}



