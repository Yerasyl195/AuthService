package kz.aparking.authservice.jwt;

import kz.aparking.authservice.user.User;
import kz.aparking.authservice.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userService.findByPhone(phone);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with phone number: " + phone);
        }
        return user;
    }
}

