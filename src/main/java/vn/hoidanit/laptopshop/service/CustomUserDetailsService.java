package vn.hoidanit.laptopshop.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //LOGIC
         vn.hoidanit.laptopshop.domain.User user=this.userService.fetchUserByEmail(username);
         if(user==null) {
             throw new UsernameNotFoundException("user not found");
         }
//        if (user == null) {
//            System.out.println("User not found for email: " + username);  // Log khi không tìm thấy người dùng
//            throw new UsernameNotFoundException("User not found");
//        } else {
//            System.out.println("User found: " + user.getEmail());  // Log khi tìm thấy người dùng
//            System.out.println("Encoded password from database: " + user.getPassword());
//            // Thực hiện so khớp mật khẩu thủ công và thêm log để kiểm tra
//            String rawPassword = "123456"; // Đặt mật khẩu người dùng nhập vào ở đây
//            boolean passwordMatch = passwordEncoder.matches(rawPassword, user.getPassword());
//            System.out.println("Password match: " + passwordMatch);  // Log kết quả so khớp
//
//        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())));



    }
}
