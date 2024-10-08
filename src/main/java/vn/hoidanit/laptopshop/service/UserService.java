package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.RegisterDTO;
import vn.hoidanit.laptopshop.repository.RoleRepository;
import vn.hoidanit.laptopshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Page<User> fetchAllUsers(Pageable page) {
        return this.userRepository.findAll(page);
    }

    public List<User> fetchAllUsersByEmail(String email) {
        return this.userRepository.findOneByEmail(email);
    }

    public User handleSaveUser(User user) {
        User eric = this.userRepository.save(user);
        System.out.println(eric);
        return eric;
    }

    public User fetchUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }
    public Role fetchRoleByName(String name){
        return this.roleRepository.findByName(name);
    }
    public User UregisterDTOtoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setFullName(registerDTO.getFirstName() +" "+ registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        return user;

    }
    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);

    }
    public User fetchUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

}
