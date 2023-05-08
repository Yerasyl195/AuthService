package kz.aparking.authservice.user;


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

    @ElementCollection
    @CollectionTable(name = "cars", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "car")
    private List<String> cars;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOrder> parkingHistory;

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getCars() {
        return cars;
    }

    public String getFullName() {
        return fullName;
    }


    public List<UserOrder> getParkingHistory() {
        return parkingHistory;
    }

    public void setParkingHistory(List<UserOrder> parkingHistory) {
        this.parkingHistory = parkingHistory;
    }


    public User(Long id, String phone, String fullName, List<String> cars) {//, List<ParkingHistory> parkingHistory) {
        this.id = id;
        this.phone = phone;
        this.fullName = fullName;
        this.cars = cars;
        //this.parkingHistory = parkingHistory;
    }

    public User() {
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCarNumbers(List<String> cars) {
        this.cars = cars;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
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
}



